package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.repository.CustomerRepository;
import com.aes.supplylinksupplychainmanagement.repository.OrderRepository;
import com.aes.supplylinksupplychainmanagement.service.IOrderDetailService;
import com.aes.supplylinksupplychainmanagement.service.IOrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private IOrderDetailService orderDetailService;

    /**
     * Creates an order for a user with specified products.
     *
     * @param user     The user for whom the order is created.
     * @param products The map of products and their quantities.
     * @return The created order.
     */
    @Override
    public Order createOrder(User user, Map<Product, Integer> products) {

        Order order = new Order();

        Customer customer = customerRepository.findByUser(user);

        order.setCustomer(customer);

        order.setOrderDate(new Date());
        int totalPrice = 0;
        for (Product product : products.keySet()) {
            totalPrice += product.getPrice() * products.get(product);
        }
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    /**
     * Finds orders by user ID.
     *
     * @param userId The ID of the user.
     * @return The list of found orders.
     */
    @Override
    public List<Order> findOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Constructs a map of orders grouped by supplier.
     *
     * @param user The user.
     * @return The constructed order map.
     */
    @Override
    public Map<Order, Map<Supplier, List<OrderDetail>>> getOrderMap(User user) {
        List<Order> orders = findOrdersByUserId(user.getUserId());
        List<OrderDetail> orderDetails = orderDetailService.findAllByOrders(orders);

        Map<Order, Map<Supplier, List<OrderDetail>>> orderMap = new HashMap<>();

        for (OrderDetail orderDetail : orderDetails) {
            Order order = orderDetail.getOrder();
            Supplier supplier = orderDetail.getProduct().getSupplier();

            Map<Supplier, List<OrderDetail>> innerMap = orderMap.computeIfAbsent(order, k -> new HashMap<>());

            List<OrderDetail> orderDetailsForSupplier = innerMap.computeIfAbsent(supplier, k -> new ArrayList<>());

            orderDetailsForSupplier.add(orderDetail);
        }
        Map<Order, Map<Supplier, List<OrderDetail>>> sortedOrderMap = orderMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Order::getOrderDate).reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        return sortedOrderMap;
    }

    /**
     * Finds an order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The found order, or null if not found.
     */
    @Override
    public Order findById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * Saves an order.
     *
     * @param order The order to save.
     */
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId    The ID of the order.
     * @param supplierId The ID of the supplier.
     * @param status     The new status of the order.
     * @return True if the status is updated successfully, false otherwise.
     */
    @Override
    public boolean updateOrderStatus(int orderId, int supplierId, OrderStatus status) {
        Order order = findById(orderId);

        order.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getProduct().getSupplier().getSupplierId() == supplierId)
                .forEach(orderDetail -> orderDetail.setStatus(status));

        boolean allDelivered = order.getOrderDetails().stream()
                .allMatch(orderDetail -> orderDetail.getStatus() == OrderStatus.DELIVERED);

        if (allDelivered)
            order.setStatus(OrderStatus.DELIVERED);
        else if (!allDelivered && order.getStatus() == OrderStatus.DELIVERED)
            order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        return true;
    }

}