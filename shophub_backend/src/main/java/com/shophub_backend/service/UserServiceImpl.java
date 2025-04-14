package com.shophub_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.model.Orders;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.UserRepository;
import com.shophub_backend.security.jwt.JwtProvider;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public List<Orders> getAllOrders() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOrders'");
    }

    @Override
    public boolean isUserPersent(String email) {
        User user = userRepository.findUserByEmail(email);
        if(user != null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User findUserByToken(String token) {
        String email = jwtProvider.getEmailFromToken(token);
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
}
