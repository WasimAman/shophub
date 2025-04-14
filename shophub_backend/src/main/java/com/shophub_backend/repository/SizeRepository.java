package com.shophub_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size,Long> {
    
}
