package com.khoi.controller;

import com.khoi.dto.request.AddCartItemRequest;
import com.khoi.dto.response.ApiResponse;
import com.khoi.entity.Cart;
import com.khoi.entity.CartItem;
import com.khoi.entity.Product;
import com.khoi.entity.User;
import com.khoi.exception.ProductException;
import com.khoi.service.CartService;
import com.khoi.service.ProductService;
import com.khoi.service.UserService;
import com.khoi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.getUserByJwtToken(token);
        Cart cart = cartService.findUserCart(user);

        System.out.println("CART - " + cart.getUser().getEmail());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddCartItemRequest request,
            @RequestHeader("Authorization") String token
    ) throws Exception, ProductException {
        User user = userService.getUserByJwtToken(token);
        Product product = productService.findProductById(request.getProductId());

        CartItem item = cartService.addCartItem(
                user,
                product,
                request.getSize(),
                request.getQuantity()
        );

        ApiResponse res = new ApiResponse();
        res.setMessage("Item add to cart successfully");
        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse<>();
        res.setMessage("Cart item removed");

        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);

        CartItem update = null;
        if(cartItem.getQuantity() > 0) {
            update = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(update, HttpStatus.ACCEPTED);
    }

}

