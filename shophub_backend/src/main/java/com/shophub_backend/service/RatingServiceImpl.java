package com.shophub_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.model.Product;
import com.shophub_backend.model.Rating;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.ProductRepository;
import com.shophub_backend.repository.RatingRepository;
import com.shophub_backend.request.RatingRequest;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private ProductService productService;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Rating rateProduct(User user, RatingRequest req) {
        Product product = productService.findProductById(req.getProductId());

        // Create new rating
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setRating(req.getRating());
        rating.setRatedAt(LocalDateTime.now());
        rating.setUser(user);

        // Save rating
        Rating savedRating = ratingRepository.save(rating);

        // Add to product's rating list (optional if you fetch lazily)
        product.getRatings().add(savedRating);

        // Calculate average rating
        double avgRating = product.getRatings().stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);

        // Save average rating to product (optional, if you have a field for it)
        product.setAvgRating(avgRating); // assuming you have this field
        productRepository.save(product);
        return savedRating;
    }

    @Override
    public List<Rating> getProductRatings(long productId) {
        Product product = productService.findProductById(productId);
        return product.getRatings();
    }

}
