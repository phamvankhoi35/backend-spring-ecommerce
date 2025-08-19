package com.khoi.controller;

import com.khoi.dto.request.CreateReviewRequest;
import com.khoi.dto.response.ApiResponse;
import com.khoi.entity.Product;
import com.khoi.entity.Review;
import com.khoi.entity.User;
import com.khoi.service.ProductService;
import com.khoi.service.ReviewService;
import com.khoi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}/review")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/product/{productId}/review")
    public ResponseEntity<Review> createReview(
            @RequestBody CreateReviewRequest request,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);
        Product product = productService.findProductById(productId);

        Review review = reviewService.createReview(request, user, product);

        return ResponseEntity.ok(review);
    }

    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest request,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);

        Review review = reviewService.updateReview(
                reviewId,
                request.getText(),
                request.getReviewRating(),
                user.getId()
        );

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserByJwtToken(token);

        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse res = new ApiResponse();
        res.setMessage("Review has been deleted");

        return ResponseEntity.ok(res);
    }
}
