package com.aes.supplylinksupplychainmanagement.service;

import com.aes.supplylinksupplychainmanagement.model.Customer;

import jakarta.validation.Valid;

public interface ICustomerService {
    /**
     * Creates a new customer.
     *
     * @param customer The customer to create.
     * @return True if the customer is created successfully, otherwise false.
     */
    boolean createCustomer(@Valid Customer customer);

    /**
     * Retrieves a customer by user ID.
     *
     * @param userId The user ID.
     * @return The customer if found, otherwise null.
     */
    Customer findByUserId(int userId);

    /**
     * Retrieves a customer with reviews and products by user ID.
     *
     * @param userId The user ID.
     * @return The customer if found, otherwise null.
     */
    Customer findWithReviewsAndProductsByUserId(int userId);

}
