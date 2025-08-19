package com.khoi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    // CascadeType.ALL : dùng để chỉ định tất cả các thao tác(lưu, cập nhật, xóa, làm mới) trên một thực thể cha sẽ được áp dụng cho các thực thể con liên quan
    // orphanRemoval : dùng để tự động xóa các đối tượng con (orphan) khi chúng không còn được liên kết với đối tượng cha (parent)
    private Set<CartItem> cartItems = new HashSet<>();

    private double totalPrice;

    private int totalItem;
    private int totalMrpPrice;
    private int discount;

    private String couponCode;

}
