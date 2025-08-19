package com.khoi.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetails {
    private String businessName;
    private String businessEmail;
    private String businessPhone;
    private String businessAddress;
    private String logo;
    private String banner;

}
