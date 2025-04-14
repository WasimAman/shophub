package com.shophub_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.User;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/admin/")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping("users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUsers = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }
}
