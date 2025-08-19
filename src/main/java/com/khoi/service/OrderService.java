package com.khoi.service;

import com.khoi.domain.OrderStatus;
import com.khoi.entity.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address addressShipping, Cart cart);
    Order findOrderById(Long orderId) throws Exception;
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(Long orderId, User user) throws Exception;
    List<Order> getOrderHistory(Long userId);
    List<Order> sellerOrder(Long sellerId);
    OrderItem findOrderItemById(Long id) throws Exception;
}
