package com.khoi.service.implement;

import com.khoi.entity.Cart;
import com.khoi.entity.CartItem;
import com.khoi.entity.Product;
import com.khoi.entity.User;
import com.khoi.repository.CartItemRepository;
import com.khoi.repository.CartRepository;
import com.khoi.service.CartService;
import com.khoi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = getUserCart(user);
        CartItem existingItem = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if(existingItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            cartItem.setSellingPrice(quantity * product.getSellingPrice());
            cartItem.setMrpPrice(quantity * product.getMrpPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            return cartItemRepository.save(cartItem);
        }

        return existingItem;
    }

    @Override
    public Cart getUserCart(User user) {
        Cart cart = cartRepository.findUserById(user.getId());

        int totalPrice = 0, totalDiscountPrice = 0, totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalPrice(totalDiscountPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));
        cart.setTotalItem(totalItem);

        return cart;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0) {
//            throw new IllegalArgumentException("Price must be greater than 0");
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;

        return (int)discountPercentage;
    }
}
