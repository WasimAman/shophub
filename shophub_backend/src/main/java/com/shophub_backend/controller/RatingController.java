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

import com.shophub_backend.model.Rating;
import com.shophub_backend.model.User;
import com.shophub_backend.request.RatingRequest;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.RatingService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/rating/")
public class RatingController {
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;


    @PostMapping("create")
    public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest req,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        Rating rating = ratingService.rateProduct(user, req);
        return ResponseEntity.status(HttpStatus.OK).body(rating);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRatingsHandler(@PathVariable long productId){
        List<Rating> productRatings = ratingService.getProductRatings(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productRatings);
    }
}
