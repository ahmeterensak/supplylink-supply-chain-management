package com.aes.supplylinksupplychainmanagement.service;

import java.util.List;

import com.aes.supplylinksupplychainmanagement.model.Review;

public interface IReviewService {
    /**
     * Finds all reviews by product ID.
     *
     * @param productId The ID of the product.
     * @return The list of found reviews.
     */
    List<Review> findAllByProductId(int productId);

    /**
     * Finds the count of reviews by product ID.
     *
     * @param productId The ID of the product.
     * @return The count of reviews.
     */
    int findCountByProductId(int productId);

    /**
     * Saves a review.
     *
     * @param review The review to save.
     */
    void save(Review review);
}
