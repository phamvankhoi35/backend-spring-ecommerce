package com.khoi.entity;

import com.khoi.domain.AccountStatus;
import com.khoi.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Embedded // khi lưu một Seller, dữ liệu của field này sẽ được lưu trữ trong cùng một bảng với Seller, thay vì tạo một bảng riêng.
    private BusinessDetails businesDetails = new BusinessDetails(); // thông tin doanh nghiệp

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private  Address pickupAdress = new Address();

    private String GSTIN; // mã số định danh người nộp thuế hàng hóa và dịch vụ

    private USER_ROLE role=USER_ROLE.ROLE_SELLER;

    private boolean isEmailVefified = false; // mặc định chưa xác minh email khi tạo tài khoản, nên cần xác minh email để sử dụng dịch vụ

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
