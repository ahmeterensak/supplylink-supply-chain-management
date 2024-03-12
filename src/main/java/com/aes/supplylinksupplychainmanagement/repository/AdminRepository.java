package com.aes.supplylinksupplychainmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /**
     * Finds an admin by user ID.
     * 
     * @param userId The ID of the user associated with the admin.
     * @return The admin entity.
     */
    @Query("SELECT a FROM Admin a JOIN a.user u WHERE u.userId = :userId")
    Admin findByUserId(@Param("userId") int userId);
}
