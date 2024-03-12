package com.aes.supplylinksupplychainmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    /**
     * Finds a supplier by email.
     *
     * @param email The email of the supplier.
     * @return An optional containing the supplier.
     */
    @Query("SELECT s FROM Supplier s JOIN s.user u WHERE u.email = :email ")
    Optional<Supplier> findByEmail(@Param("email") String email);

    /**
     * Finds a supplier with its products by user ID.
     *
     * @param userId The ID of the user.
     * @return The supplier.
     */
    @Query("SELECT DISTINCT s FROM Supplier s LEFT JOIN FETCH s.products p JOIN s.user u WHERE u.userId = :userId")
    Supplier findSupplierWithProductsByUserId(@Param("userId") int userId);

    /**
     * Finds orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return The list of orders.
     */
    @Query("SELECT DISTINCT od.order FROM OrderDetail od " +
            "JOIN od.product p " +
            "JOIN p.supplier s " +
            "WHERE s.supplierId = :supplierId " +
            "AND od.order IN (SELECT od1.order FROM OrderDetail od1 " +
            "JOIN od1.product p1 " +
            "JOIN p1.supplier s1 " +
            "WHERE s1 = s) " +
            "ORDER BY od.order.orderDate ASC")
    List<Order> findOrdersBySupplierId(@Param("supplierId") int supplierId);

    /**
     * Finds a supplier by user ID.
     *
     * @param userId The ID of the user.
     * @return The supplier.
     */
    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.user u WHERE u.userId = :userId")
    Supplier findSupplierByUserId(@Param("userId") int userId);

    /**
     * Finds suppliers by order details.
     *
     * @param orderDetails The list of order details.
     * @return The list of suppliers.
     */
    @Query("SELECT DISTINCT p.supplier FROM OrderDetail od JOIN od.product p WHERE od IN :orderDetails")
    List<Supplier> findSuppliersByOrderDetails(@Param("orderDetails") List<OrderDetail> orderDetails);

    /**
     * Finds completed orders by supplier ID.
     *
     * @param supplierId The ID of the supplier.
     * @return The list of completed orders.
     */
    List<Order> findCompletedOrdersBySupplierId(int supplierId);
}