package com.khoi.repository;

import com.khoi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findUserById(Long id);
}
