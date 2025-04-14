package com.shophub_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    public User findUserByEmail(String email);
}
