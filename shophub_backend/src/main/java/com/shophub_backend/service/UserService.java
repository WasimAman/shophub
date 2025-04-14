package com.shophub_backend.service;

import java.util.List;

import com.shophub_backend.model.Orders;
import com.shophub_backend.model.User;

public interface UserService {
    public List<Orders> getAllOrders();

    public boolean isUserPersent(String email);

    public User findUserByToken(String token);
    public List<User> findAllUsers();
}
