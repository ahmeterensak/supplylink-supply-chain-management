package com.aes.supplylinksupplychainmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.model.User;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Finds a customer by email.
     *
     * @param email The email of the customer.
     * @return Optional containing the customer entity, if found.
     */
    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.email = :email ")
    Optional<Customer> findByEmail(@Param("email") String email);

    /**
     * Finds a customer by user.
     *
     * @param user The user associated with the customer.
     * @return The customer entity.
     */
    Customer findByUser(User user);

    /**
     * Finds a customer by user ID.
     *
     * @param userId The ID of the user associated with the customer.
     * @return The customer entity.
     */
    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.userId = :userId")
    Customer findByUserId(@Param("userId") int userId);

    /**
     * Finds a customer with their reviews and associated products by user ID.
     *
     * @param userId The ID of the user associated with the customer.
     * @return The customer entity with reviews and products.
     */
    @Query("SELECT c FROM Customer c JOIN FETCH c.reviews r JOIN FETCH r.product p JOIN c.user u WHERE u.userId = :userId")
    Customer findWithReviewsAndProductsByUserId(int userId);
}