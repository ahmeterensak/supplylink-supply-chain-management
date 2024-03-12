package com.aes.supplylinksupplychainmanagement.service;

import java.util.List;
import java.util.Map;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Supplier;

public interface ISupplierService {
    /**
     * Creates a new supplier.
     *
     * @param supplier The supplier to create.
     * @return True if the supplier is successfully created, false otherwise.
     */
    boolean createSupplier(Supplier supplier);

    /**
     * Finds a supplier with products by user ID.
     *
     * @param userId The ID of the user.
     * @return The supplier with products.
     */
    Supplier findSupplierWithProductsByUserId(int userId);

    /**
     * Finds a supplier by user ID.
     *
     * @param userId The ID of the user.
     * @return The found supplier.
     */
    Supplier findSupplierByUserId(int userId);

    /**
     * Updates a supplier.
     *
     * @param supplier The supplier to update.
     */
    void update(Supplier supplier);

    /**
     * Finds orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return A map containing orders by their status.
     */
    Map<OrderStatus, List<Order>> findOrdersBySupplierId(int supplierId);

    /**
     * Finds completed orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return A list of completed orders.
     */
    List<Order> findCompletedOrdersBySupplierId(int supplierId);

    /**
     * Sets orders to suppliers.
     *
     * @param order        The order to set.
     * @param orderDetails The order details.
     */
    void setOrdersToSuppliers(Order order, List<OrderDetail> orderDetails);
}
