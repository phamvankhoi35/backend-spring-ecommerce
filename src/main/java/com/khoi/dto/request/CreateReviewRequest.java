package com.khoi.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateReviewRequest {
    private String text;
    private double reviewRating;
    private List<String> productImage;
}
