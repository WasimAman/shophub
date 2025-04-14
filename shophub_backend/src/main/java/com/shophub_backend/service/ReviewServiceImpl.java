package com.shophub_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.model.Product;
import com.shophub_backend.model.Review;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.ReviewRepository;
import com.shophub_backend.request.ReviewRequest;
@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Override
    public Review reviewProduct(User user, ReviewRequest req) {
        Product product = productService.findProductById(req.getProductId());

        Review review = new Review();
        review.setProduct(product);
        review.setReviewedAt(LocalDateTime.now());
        review.setUser(user);
        review.setReview(req.getReview());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getProductReviews(long productId) {
        Product product = productService.findProductById(productId);
        return product.getReviews();
    }
    
}
