package com.khoi.service;

import com.khoi.entity.Product;
import com.khoi.entity.User;
import com.khoi.entity.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
