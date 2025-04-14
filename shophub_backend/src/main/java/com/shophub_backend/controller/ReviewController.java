package com.shophub_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.Review;
import com.shophub_backend.model.User;
import com.shophub_backend.request.ReviewRequest;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.ReviewService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/review/")
public class ReviewController {

    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping("create")
    public ResponseEntity<Review> createReviewHandler(@RequestBody ReviewRequest req,@RequestHeader(JwtConstant.JWT_HEADER) String token){

        User user = userService.findUserByToken(token);
        Review review = reviewService.reviewProduct(user, req);
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }


    @GetMapping("product/{productId}")
    public ResponseEntity<List<Review>> getProductReviewshandler(@PathVariable long productId){
        List<Review> productReviews = reviewService.getProductReviews(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productReviews);
    }
    
}
