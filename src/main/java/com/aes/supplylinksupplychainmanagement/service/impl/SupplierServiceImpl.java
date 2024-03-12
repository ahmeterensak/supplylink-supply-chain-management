package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.repository.SupplierRepository;
import com.aes.supplylinksupplychainmanagement.service.ISupplierService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

    private SupplierRepository supplierRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new supplier.
     *
     * @param supplier The supplier to create.
     * @return True if the supplier is successfully created, false otherwise.
     */
    @Override
    public boolean createSupplier(Supplier supplier) {
        Optional<Supplier> optionalSupplier = supplierRepository.findByEmail(supplier.getUser().getEmail());
        if (optionalSupplier.isPresent())
            return false;

        supplier.getUser().setPassword(passwordEncoder.encode(supplier.getUser().getPassword()));
        supplierRepository.save(supplier);
        return true;
    }

    /**
     * Finds a supplier with products by user ID.
     *
     * @param userId The ID of the user.
     * @return The supplier with products.
     */
    @Override
    public Supplier findSupplierWithProductsByUserId(int userId) {
        Supplier supplier = supplierRepository.findSupplierWithProductsByUserId(userId);
        supplier.setProducts(Optional.ofNullable(supplier.getProducts()).orElse(List.of()));
        return supplier;
    }

    /**
     * Updates a supplier.
     *
     * @param supplier The supplier to update.
     */
    @Override
    public void update(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    /**
     * Finds a supplier by user ID.
     *
     * @param userId The ID of the user.
     * @return The found supplier.
     */
    @Override
    public Supplier findSupplierByUserId(int userId) {
        return supplierRepository.findSupplierByUserId(userId);
    }

    /**
     * Sets orders to suppliers.
     *
     * @param order        The order to set.
     * @param orderDetails The order details.
     */
    @Override
    public void setOrdersToSuppliers(Order order, List<OrderDetail> orderDetails) {
        List<Supplier> suppliers = supplierRepository.findSuppliersByOrderDetails(orderDetails);
        for (Supplier supplier : suppliers) {
            if (supplier.getOrders() == null)
                supplier.setOrders(new ArrayList<Order>());
            supplier.getOrders().add(order);
        }
        supplierRepository.saveAll(suppliers);
    }

    /**
     * Finds orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return A map containing orders by their status.
     */
    @Override
    public Map<OrderStatus, List<Order>> findOrdersBySupplierId(int supplierId) {
        List<Order> orders = supplierRepository.findOrdersBySupplierId(supplierId);
        Map<OrderStatus, List<Order>> orderMap = new HashMap<>();

        for (Order order : orders) {
            order.getOrderDetails()
                    .removeIf(orderDetail -> orderDetail.getProduct().getSupplier().getSupplierId() != supplierId);
            List<Order> orderList = orderMap.computeIfAbsent(order.getStatus(), k -> new ArrayList<>());
            orderList.add(order);
        }
        return orderMap;
    }

    /**
     * Finds completed orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return A list of completed orders.
     */
    @Override
    public List<Order> findCompletedOrdersBySupplierId(int supplierId) {
        return supplierRepository.findCompletedOrdersBySupplierId(supplierId);
    }

}