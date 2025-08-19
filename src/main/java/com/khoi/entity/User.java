package com.khoi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khoi.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode // tự động tạo các phương thức equals() và hashCode()
// @Data // tự tạo getter setter equals(), hashCode(), toString()
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // write_only : chỉ đọc từ json tạo mới, không cho phép hiện khi lấy json data
    private String password;

    private String email;

    private String fullName;

    private String phone;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> address = new HashSet<>();

    @ManyToMany
    @JsonIgnore // xác định field sẽ không xuất hiện trong JSON đầu ra và cũng không được sử dụng để tạo đối tượng JSON đầu vào
    private Set<Coupon> usedCoupons = new HashSet<>();
}
