package com.aes.supplylinksupplychainmanagement.model;

import org.hibernate.annotations.GenericGenerator;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private int orderDetailId;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH, }, targetEntity = Order.class)
        @JoinColumn(name = "order_id", referencedColumnName = "orderId")
        private Order order;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH, }, targetEntity = Product.class)
        @JoinColumn(name = "product_id", referencedColumnName = "productId")
        private Product product;

        private OrderStatus status;

        @NotNull
        private int quantity;

        @NotNull
        private int unitPrice;

        @NotNull
        private int totalPrice;
}
