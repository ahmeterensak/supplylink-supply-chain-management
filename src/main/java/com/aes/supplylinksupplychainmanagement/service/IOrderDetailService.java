package com.aes.supplylinksupplychainmanagement.service;

import java.util.List;
import java.util.Map;

import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;

public interface IOrderDetailService {
    /**
     * Creates order details based on cart items and an order.
     *
     * @param cartItems The map of cart items.
     * @param order     The order.
     * @return The list of created order details.
     */
    List<OrderDetail> createOrderDetails(Map<String, Integer> cartItems, Order order);

    /**
     * Finds all order details by a list of orders.
     *
     * @param orders The list of orders.
     * @return The list of found order details.
     */
    List<OrderDetail> findAllByOrders(List<Order> orders);

}
