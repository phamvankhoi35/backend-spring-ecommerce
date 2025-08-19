package com.khoi.controller;

import com.khoi.domain.OrderStatus;
import com.khoi.entity.Order;
import com.khoi.entity.Seller;
import com.khoi.exception.SellerException;
import com.khoi.service.OrderService;
import com.khoi.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/order")
public class SellerOrderController {
    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(token);
        List<Order> orders = orderService.sellerOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws Exception {
        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

}
