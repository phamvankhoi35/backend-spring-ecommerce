package com.khoi.controller;

import com.khoi.entity.Cart;
import com.khoi.entity.Coupon;
import com.khoi.entity.User;
import com.khoi.service.CartService;
import com.khoi.service.CouponService;
import com.khoi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
        @RequestParam String apply,
        @RequestParam String code,
        @RequestParam double orderValue,
        @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);

        Cart cart;

        if(apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(cart);
    }

    @GetMapping("/admin/all")
    public  ResponseEntity<List<Coupon>> getAllCoupon() {
        List<Coupon> coupons = couponService.getAllCoupon();

        return ResponseEntity.ok(coupons);
    }

    @PostMapping("/admin/create")
    public  ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon newCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(newCoupon);
    }

    @DeleteMapping("/admin/remove/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon has been remove");
    }
}
