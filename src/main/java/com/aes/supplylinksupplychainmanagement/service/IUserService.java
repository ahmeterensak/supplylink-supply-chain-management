package com.aes.supplylinksupplychainmanagement.service;

import com.aes.supplylinksupplychainmanagement.model.Admin;
import com.aes.supplylinksupplychainmanagement.model.User;

public interface IUserService {
    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The found user or null if not found.
     */
    User findUserByEmail(String email);

    /**
     * Finds an admin by user ID.
     *
     * @param userId The ID of the user.
     * @return The found admin.
     */
    Admin findAdminByUserId(int userId);
}
