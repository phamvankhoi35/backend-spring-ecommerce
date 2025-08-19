package com.khoi.service;

import com.khoi.entity.Order;
import com.khoi.entity.PaymentOrder;
import com.khoi.entity.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(Long orderId) throws Exception;

    Boolean ProceedPaymentOrder(
            PaymentOrder paymentOrder,
            Long paymentId,
            Long paymentLinkIdLong
    ) throws RazorpayException;
    PaymentLink createRazorpayPatmentLink(User user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
