package com.khoi.service;

import com.khoi.entity.Cart;
import com.khoi.entity.CartItem;
import com.khoi.entity.Product;
import com.khoi.entity.User;

public interface CartService {
    CartItem addCartItem(User user, Product product, String size, int quantity);
    Cart getUserCart(User user);
}
