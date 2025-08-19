package com.khoi.service;

import com.khoi.entity.Cart;
import com.khoi.entity.Coupon;
import com.khoi.entity.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon getCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> getAllCoupon();
    void deleteCoupon(Long id) throws Exception;
}
