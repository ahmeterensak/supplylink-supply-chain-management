package com.aes.supplylinksupplychainmanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    /**
     * Finds all order details by orders.
     *
     * @param orders The list of orders.
     * @return The list of order details.
     */
    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.order o JOIN FETCH od.product p JOIN FETCH p.supplier s WHERE od.order IN :orders")
    List<OrderDetail> findAllByOrders(List<Order> orders);

}