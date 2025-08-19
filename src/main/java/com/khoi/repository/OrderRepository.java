package com.khoi.repository;

import com.khoi.entity.Order;
import com.khoi.utils.OtpUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderById(Long orderId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findByUserId(Long userId);

}
