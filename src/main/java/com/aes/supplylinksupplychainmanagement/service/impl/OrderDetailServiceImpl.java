package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.repository.OrderDetailRepository;
import com.aes.supplylinksupplychainmanagement.repository.ProductRepository;
import com.aes.supplylinksupplychainmanagement.service.IOrderDetailService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private OrderDetailRepository orderDetailRepository;
    private ProductRepository productRepository;

    /**
     * Creates order details based on cart items and an order.
     *
     * @param cartItems The map of cart items.
     * @param order     The order.
     * @return The list of created order details.
     */
    @Override
    public List<OrderDetail> createOrderDetails(Map<String, Integer> cartItems, Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (String slug : cartItems.keySet()) {
            Product product = productRepository.findBySlug(slug);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItems.get(slug));
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setTotalPrice(product.getPrice() * cartItems.get(slug));
            orderDetail.setStatus(OrderStatus.PENDING);
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            orderDetails.add(savedOrderDetail);
        }
        return orderDetails;
    }

    /**
     * Finds all order details by a list of orders.
     *
     * @param orders The list of orders.
     * @return The list of found order details.
     */
    @Override
    public List<OrderDetail> findAllByOrders(List<Order> orders) {
        return orderDetailRepository.findAllByOrders(orders);
    }

}