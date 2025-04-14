package com.shophub_backend.service;

import java.util.List;

import com.shophub_backend.model.Review;
import com.shophub_backend.model.User;
import com.shophub_backend.request.ReviewRequest;

public interface ReviewService {
    public Review reviewProduct(User user,ReviewRequest req);
    public List<Review> getProductReviews(long productId);
}
