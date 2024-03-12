package com.aes.supplylinksupplychainmanagement.service;

import java.util.List;
import java.util.Map;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.model.User;

public interface IOrderService {
    /**
     * Creates an order for a user with specified products.
     *
     * @param user     The user for whom the order is created.
     * @param products The map of products and their quantities.
     * @return The created order.
     */
    Order createOrder(User user, Map<Product, Integer> products);

    /**
     * Finds orders by user ID.
     *
     * @param userId The ID of the user.
     * @return The list of found orders.
     */
    List<Order> findOrdersByUserId(int userId);

    /**
     * Constructs a map of orders grouped by supplier.
     *
     * @param user The user.
     * @return The constructed order map.
     */
    Map<Order, Map<Supplier, List<OrderDetail>>> getOrderMap(User user);

    /**
     * Finds an order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The found order, or null if not found.
     */
    Order findById(int orderId);

    /**
     * Saves an order.
     *
     * @param order The order to save.
     */
    void save(Order order);

    /**
     * Updates the status of an order.
     *
     * @param orderId    The ID of the order.
     * @param supplierId The ID of the supplier.
     * @param status     The new status of the order.
     * @return True if the status is updated successfully, false otherwise.
     */
    boolean updateOrderStatus(int orderId, int supplierId, OrderStatus status);

}
