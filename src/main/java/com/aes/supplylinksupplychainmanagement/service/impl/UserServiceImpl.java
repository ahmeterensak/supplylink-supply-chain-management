package com.aes.supplylinksupplychainmanagement.service.impl;

import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.model.Admin;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.repository.AdminRepository;
import com.aes.supplylinksupplychainmanagement.repository.UserRepository;
import com.aes.supplylinksupplychainmanagement.service.IUserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private AdminRepository adminRepository;

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The found user or null if not found.
     */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Finds an admin by user ID.
     *
     * @param userId The ID of the user.
     * @return The found admin.
     */
    @Override
    public Admin findAdminByUserId(int userId) {
        return adminRepository.findByUserId(userId);
    }
}