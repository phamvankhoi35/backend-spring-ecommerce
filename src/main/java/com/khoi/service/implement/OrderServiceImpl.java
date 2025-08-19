package com.khoi.service.implement;

import com.khoi.domain.OrderStatus;
import com.khoi.domain.PaymentStatus;
import com.khoi.entity.*;
import com.khoi.repository.AddressRepository;
import com.khoi.repository.OrderItemRepository;
import com.khoi.repository.OrderRepository;
import com.khoi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddress().contains(shippingAddress)) {
            user.getAddress().add(shippingAddress);
        }

        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItem>> itemBySeller = cart
                .getCartItems()
                .stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry: itemBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalItem = items.stream().mapToInt(
                   CartItem::getQuantity
            ).sum();

            Order newOrder = new Order();
            newOrder.setUser(user);
            newOrder.setSellerId(sellerId);
            newOrder.setTotalMrpPrice(totalOrderPrice);
            newOrder.setTotalSellingPrice(totalOrderPrice);
            newOrder.setTotalItem(totalItem);
            newOrder.setShippingAddress(address);
            newOrder.setOrderStatus(OrderStatus.PENDING);

            newOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order saveOrder = orderRepository.save(newOrder);
            orders.add(saveOrder);

            List<OrderItem> orderItems = new ArrayList<>();
            for(CartItem item: items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(saveOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setSellingPrice(item.getSellingPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());

                saveOrder.getOrderItem().add(orderItem);

                OrderItem saveOrderItem = orderItemRepository.save(orderItem);

                orderItems.add(saveOrderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
    }

    @Override
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())) {
            throw new Exception("You do not have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(Long id) throws Exception {
        return orderItemRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Order item not exist"));
    }
}
