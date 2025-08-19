package com.khoi.entity;


import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    //    private String bankName;
    private String ifscCode;

}
