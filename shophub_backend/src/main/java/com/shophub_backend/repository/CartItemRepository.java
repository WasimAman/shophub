package com.shophub_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    @Query("SELECT ci FROM CartItems ci WHERE ci.cart = :cart AND ci.product = :product AND ci.size = :size AND ci.userId = :userId")
    public CartItems isCartItemExist(
            @Param("cart") Cart cart,
            @Param("product") Product product,
            @Param("size") String size,
            @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItems c WHERE c.product.id = :productId")
    public void deleteByProductId(@Param("productId") long productId);

    @Query("SELECT ci FROM CartItems ci WHERE ci.product.id = :productId")
    List<CartItems> findAllByProductId(@Param("productId") long productId);

    // @Query("SELECT ci FROM CartItems WHERE ci.product.id = :productId AND ci.userId = :userId")
    // public CartItems removeCartItemFromCart(@Param("productId") long productId,@Param("userId") long userId);

    CartItems findByUserIdAndProductId(long userId, long productId);
}
