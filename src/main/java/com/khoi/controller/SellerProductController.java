package com.khoi.controller;

import com.khoi.dto.request.CreateProductRequest;
import com.khoi.entity.Product;
import com.khoi.entity.Seller;
import com.khoi.exception.ProductException;
import com.khoi.exception.SellerException;
import com.khoi.service.ProductService;
import com.khoi.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/product")
public class SellerProductController {

    private final SellerService sellerService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> getPRoductBySellerId(
            @RequestHeader("Authorization") String token
    ) throws ProductException, SellerException {
        Seller sellers = sellerService.getSellerProfile(token);
        List<Product> products = productService.getProductBySellerId(sellers.getId());

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization") String token
    ) throws ProductException, SellerException {
        Seller seller = sellerService.getSellerProfile(token);
        Product product = productService.createProduct(request, seller);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(
            @PathVariable Long productId
    ) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product
    ) throws ProductException {
        try {
            Product update = productService.updateProduct(productId, product);
            return new ResponseEntity<>(update, HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
