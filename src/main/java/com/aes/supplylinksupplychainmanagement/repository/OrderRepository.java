package com.aes.supplylinksupplychainmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Finds orders by user ID.
     *
     * @param userId The ID of the user.
     * @return The list of orders.
     */
    @Query("SELECT o FROM Order o JOIN o.customer c JOIN c.user u WHERE u.userId = :userId")
    List<Order> findByUserId(@Param("userId") int userId);
}