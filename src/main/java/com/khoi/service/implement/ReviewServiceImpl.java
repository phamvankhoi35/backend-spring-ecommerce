package com.khoi.service.implement;

import com.khoi.dto.request.CreateReviewRequest;
import com.khoi.entity.Product;
import com.khoi.entity.Review;
import com.khoi.entity.User;
import com.khoi.repository.ReviewRepository;
import com.khoi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest request, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(request.getText());
        review.setRating(request.getReviewRating());
        review.setProductImages(request.getProductImage());

        product.getReviews().add(review);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findReviewByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String text, double rating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            review.setReviewText(text);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new Exception("You can not update review");
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new Exception("Review not found"));
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            throw new Exception("You can not delete this review");
        }
        reviewRepository.delete(review);
    }
}
