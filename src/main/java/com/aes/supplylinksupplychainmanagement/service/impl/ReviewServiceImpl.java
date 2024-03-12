package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.model.Review;
import com.aes.supplylinksupplychainmanagement.repository.ReviewRepository;
import com.aes.supplylinksupplychainmanagement.service.IReviewService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private ReviewRepository reviewRepository;

    /**
     * Finds all reviews by product ID.
     *
     * @param productId The ID of the product.
     * @return The list of found reviews.
     */
    @Override
    public List<Review> findAllByProductId(int productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    /**
     * Finds the count of reviews by product ID.
     *
     * @param productId The ID of the product.
     * @return The count of reviews.
     */
    @Override
    public int findCountByProductId(int productId) {
        return reviewRepository.findCountByProductId(productId);
    }

    /**
     * Saves a review.
     *
     * @param review The review to save.
     */
    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }

}