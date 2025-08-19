package com.khoi.service;

import com.khoi.dto.request.CreateReviewRequest;
import com.khoi.entity.Product;
import com.khoi.entity.Review;
import com.khoi.entity.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest request, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String text, double rating, Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
    void deleteReview(Long reviewId, Long userId) throws Exception;

}
