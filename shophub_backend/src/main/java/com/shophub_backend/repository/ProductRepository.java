package com.shophub_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shophub_backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        @Query("SELECT p FROM Product p WHERE LOWER(p.category.name) = LOWER(:categoryName)")
        public List<Product> findProductByCategory(@Param("categoryName") String categoryName);

        @Query("SELECT p FROM Product p WHERE " +
                        "LOWER(p.title) LIKE %:keyword% OR " +
                        "LOWER(p.description) LIKE %:keyword% OR " +
                        "LOWER(p.brand) LIKE %:keyword% OR " +
                        "LOWER(p.category.name) LIKE %:keyword%")
        List<Product> searchProducts(@Param("keyword") String keyword);

        @Query("SELECT p FROM Product p " +
                        "WHERE (:category = '' OR p.category.name = :category) " +
                        "AND ((:minPrice IS NULL OR :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) "
                        +
                        "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount)")
        List<Product> filterProducts(
                        @Param("category") String category,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice,
                        @Param("minDiscount") Integer minDiscount);

        public List<Product> findTop10ByOrderByCreatedAtDesc();

}
