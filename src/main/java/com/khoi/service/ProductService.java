package com.khoi.service;

import com.khoi.dto.request.CreateProductRequest;
import com.khoi.entity.Product;
import com.khoi.entity.Seller;
import com.khoi.exception.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest request, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long id) throws ProductException;
    List<Product> searchProduct(String query);
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
    );
    List<Product> getProductBySellerId(Long sellerId);
}
