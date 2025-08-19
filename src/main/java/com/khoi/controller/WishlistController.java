package com.khoi.controller;


import com.khoi.entity.Product;
import com.khoi.entity.User;
import com.khoi.entity.Wishlist;
import com.khoi.exception.ProductException;
import com.khoi.service.ProductService;
import com.khoi.service.UserService;
import com.khoi.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

//    @PostMapping("/add")
//    public ResponseEntity<Wishlist> createWishlist(@RequestBody User user) {
//        Wishlist wishlist = wishlistService.createWishlist(user);
//        return ResponseEntity.ok(wishlist);
//    }

    @GetMapping
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);

        Wishlist wishlist = wishlistService.getWishlistByUserId(user);

        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        Product product = productService.findProductById(productId);
        User  user = userService.getUserByJwtToken(token);
        Wishlist updateWishlist = wishlistService.addProductToWishlist(user, product);

        return ResponseEntity.ok(updateWishlist);
    }
}
