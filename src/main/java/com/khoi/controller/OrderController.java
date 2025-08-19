package com.khoi.controller;

import com.khoi.domain.PaymentMethod;
import com.khoi.dto.response.PaymentLinkResponse;
import com.khoi.entity.*;
import com.khoi.repository.PaymentOrderRepository;
import com.khoi.service.*;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    // service
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;

    // repository
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrder(
        @RequestBody Address shippingAdress,
        @RequestParam PaymentMethod paymentMethod,
        @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);
        Cart cart = cartService.getUserCart(user);
        Set<Order> orders = orderService.createOrder(user, shippingAdress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse res = new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink payment = paymentService.createRazorpayPatmentLink(
                    user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId()
            );

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            res.setPayment_link_url(paymentUrl);

            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(
                    user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId()
            );
            res.setPayment_link_url(paymentUrl);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrderHistory(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);
        List<Order> orders = orderService.getOrderHistory(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
//        User user = userService.findUserByJwtToken(token);
        Order orders = orderService.findOrderById(orderId);

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
//        User user = userService.findUserByJwtToken(token);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);

        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        // hàm này huỷ đơn hàng
        // ko được xử dụng delete để xoá
        // chỉ cập nhật trạng thái đơn hàng sang CANLELLED.
        User user = userService.getUserByJwtToken(token);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
