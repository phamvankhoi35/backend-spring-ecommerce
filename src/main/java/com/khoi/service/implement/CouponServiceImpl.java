package com.khoi.service.implement;

import com.khoi.entity.Cart;
import com.khoi.entity.Coupon;
import com.khoi.entity.User;
import com.khoi.repository.CartRepository;
import com.khoi.repository.CouponRepository;
import com.khoi.repository.UserRepository;
import com.khoi.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);

        Cart cart = cartRepository.findUserById(user.getId());

        if(coupon == null) {
            throw new Exception("Coupon not valid");
        }

        if(user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon already used");
        }

        if(orderValue <= coupon.getMinimumOrder()) {
            throw new Exception("Coupon less than minimum order value " + coupon.getMinimumOrder());
        }

        if(
                coupon.isActive()
                && LocalDate.now().isAfter(coupon.getStartDate())
                && LocalDate.now().isBefore(coupon.getEndDate())
        ) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountPrice = ( cart.getTotalPrice() * coupon.getDiscountPercentage() ) / 100;

            cart.setTotalPrice(cart.getTotalPrice() - discountPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);

        if(coupon == null) {
            throw new Exception("Coupon not found");
        }

        Cart cart = cartRepository.findUserById(user.getId());

        double discountPrice = ( cart.getTotalPrice() * coupon.getDiscountPercentage() ) / 100;

        cart.setTotalPrice(cart.getTotalPrice() + discountPrice);
        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    public Coupon getCouponById(Long couponId) throws Exception {
        return couponRepository
                .findById(couponId)
                .orElseThrow(() -> new Exception("Coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long couponId) throws Exception {
        getCouponById(couponId);
        couponRepository.deleteById(couponId);
    }
}
