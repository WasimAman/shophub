package com.shophub_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>{
    
}
