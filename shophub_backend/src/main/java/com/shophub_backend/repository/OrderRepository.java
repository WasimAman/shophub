package com.shophub_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long>{
    List<Orders> findAllByOrderByOrderDateTimeDesc();
}