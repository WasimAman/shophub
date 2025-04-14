package com.shophub_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.User;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> userProfile(@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
