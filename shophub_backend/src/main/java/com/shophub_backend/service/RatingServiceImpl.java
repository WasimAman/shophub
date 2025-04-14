package com.shophub_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.model.Product;
import com.shophub_backend.model.Rating;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.RatingRepository;
import com.shophub_backend.request.RatingRequest;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    private ProductService productService;
    @Autowired
    private RatingRepository ratingRepository;
    @Override
    public Rating rateProduct(User user, RatingRequest req) {
        Product product = productService.findProductById(req.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setRating(req.getRating());
        rating.setRatedAt(LocalDateTime.now());
        rating.setUser(user);
        product.getRatings().add(rating);

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRatings(long productId) {
        Product product = productService.findProductById(productId);
        return product.getRatings();
    }
    
}
 