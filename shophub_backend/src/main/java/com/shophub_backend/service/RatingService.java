package com.shophub_backend.service;

import java.util.List;

import com.shophub_backend.model.Rating;
import com.shophub_backend.model.User;
import com.shophub_backend.request.RatingRequest;

public interface RatingService {
    public Rating rateProduct(User user,RatingRequest req);
    public List<Rating> getProductRatings(long productId);
}
