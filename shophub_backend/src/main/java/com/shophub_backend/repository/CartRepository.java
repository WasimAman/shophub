package com.shophub_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findByUserId(long userId);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    public Optional<Cart> findUserCart(@Param("userId") long userId);
}
