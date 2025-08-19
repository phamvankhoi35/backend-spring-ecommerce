package com.khoi.dto.request;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Long productId;
    private String size;
    private int quantity;
}
