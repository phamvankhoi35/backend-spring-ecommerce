package com.khoi.repository;

import com.khoi.entity.Cart;
import com.khoi.entity.CartItem;
import com.khoi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
