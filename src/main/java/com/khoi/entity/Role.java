package com.khoi.entity;

import com.khoi.domain.USER_ROLE;
import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private USER_ROLE role;
}
