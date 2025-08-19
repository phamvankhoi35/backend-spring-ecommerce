package com.khoi.service.implement;

import com.khoi.dto.request.CreateProductRequest;
import com.khoi.entity.Category;
import com.khoi.entity.Product;
import com.khoi.entity.Seller;
import com.khoi.exception.ProductException;
import com.khoi.repository.CategoryRepository;
import com.khoi.repository.ProductRepository;
import com.khoi.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        // tạo category trước khi tạo product

//        Category existCategory = categoryRepository.findByName(request.getCategory());

        // Level 1 category
        Category category1 = categoryRepository.findByCategoryId(request.getCategory());
        if(category1 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory());
            category.setLevel(1);
            category.setName(request.getCategory());
            category1 = categoryRepository.save(category);
        }
        // Level 2 category
        Category category2 = categoryRepository.findByCategoryId(request.getCategory2());
        if(category2 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category.setName(request.getCategory2());
            category2 = categoryRepository.save(category);
        }
        // Level 3 category
        Category category3 = categoryRepository.findByCategoryId(request.getCategory3());
        if(category3 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category.setName(request.getCategory3());
            category3 = categoryRepository.save(category);
        }

        // tạo product
        int disountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setSellingPrice(product.getSellingPrice());
        product.setImage(request.getImages());
        product.setMrpPrice(request.getMrpPrice());
        product.setSize(request.getSize());
        product.setDiscountPercent(disountPercentage);
        product.setCreateAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0) {
//            throw new IllegalArgumentException("Price must be greater than 0");
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;

        return (int)discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductException("Product {" + productId + "} not found"));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProduct(
            String category,
            String brand,
            String color,
            String size,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    ) {
        Specification<Product> specification = (
                root,
                query,
                criteriaBuilder
        ) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            if(color != null && !color.isEmpty()) {
                System.out.println("color : " + color);
                predicates.add(criteriaBuilder.equal(root.get("color"), color));
            }
            // filter by size
            if(size != null && !size.isEmpty()) {
                System.out.println("size : " + size);
                predicates.add(criteriaBuilder.equal(root.get("size"), size));
            }
            if(minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if(maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if(minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if(stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

        Pageable pageable;
        if(sort != null && !sort.isEmpty()) {
            Sort sortOrder = switch (sort) {
                case "price_low" -> Sort.by("sellingPrice").ascending();
                case "price_high" -> Sort.by("sellingPrice").descending();
                default -> Sort.unsorted();
            };
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, sortOrder);
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }

//        if(sort != null && !sort.isEmpty())  {
//            pageable = switch (sort) {
//                case "price_low" ->
//                    pageable = PageRequest.of(
//                            pageNumber != null ? pageNumber : 0,
//                            10,
//                            Sort.by("sellingPrice").ascending()
//                    );
//
//                case "price_high" ->
//                    pageable = PageRequest.of(
//                            pageNumber != null ? pageNumber : 0,
//                            10,
//                            Sort.by("sellingPrice").descending()
//                    );
//                default ->
//                    pageable = PageRequest.of(
//                            pageNumber != null ? pageNumber : 0,
//                            10,
//                            Sort.unsorted()
//                    );
//            };
//        } else {
//            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
//        }

        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
