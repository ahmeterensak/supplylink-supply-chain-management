package com.aes.supplylinksupplychainmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /**
     * Finds all reviews for a given product ID.
     *
     * @param productId The ID of the product.
     * @return The list of reviews.
     */
    @Query("SELECT c FROM Review c LEFT JOIN FETCH c.customer cu JOIN c.product p WHERE p.productId = :productId")
    List<Review> findAllByProductId(@Param("productId") int productId);

    /**
     * Finds the count of reviews for a given product ID.
     *
     * @param productId The ID of the product.
     * @return The count of reviews.
     */
    @Query("SELECT Count(c) FROM Review c JOIN c.product p WHERE p.productId = :productId")
    int findCountByProductId(@Param("productId") int productId);
}