package com.khoi.service;

import com.khoi.entity.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception;
    void removeCartItem(Long userId, Long cartItemId) throws Exception;
    CartItem getCartItemById(Long id) throws Exception;
}
