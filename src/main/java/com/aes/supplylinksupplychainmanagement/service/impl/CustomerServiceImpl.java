package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.repository.CustomerRepository;
import com.aes.supplylinksupplychainmanagement.service.ICustomerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new customer.
     *
     * @param customer The customer to create.
     * @return         True if the customer is created successfully, otherwise false.
     */
    @Override
    public boolean createCustomer(@Valid Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getUser().getEmail());
        if (optionalCustomer.isPresent()) {
            return false;
        }

        customer.getUser().setPassword(passwordEncoder.encode(customer.getUser().getPassword()));
        customerRepository.save(customer);
        return true;
    }

    /**
     * Retrieves a customer by user ID.
     *
     * @param userId The user ID.
     * @return       The customer if found, otherwise null.
     */
    @Override
    public Customer findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    /**
     * Retrieves a customer with reviews and products by user ID.
     *
     * @param userId The user ID.
     * @return       The customer if found, otherwise null.
     */
    @Override
    public Customer findWithReviewsAndProductsByUserId(int userId) {
        return customerRepository.findWithReviewsAndProductsByUserId(userId);
    }
}