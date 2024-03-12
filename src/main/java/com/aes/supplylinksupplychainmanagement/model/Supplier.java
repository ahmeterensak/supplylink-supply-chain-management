package com.aes.supplylinksupplychainmanagement.model;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private int supplierId;

        @NotBlank(message = "Company name cannot be blank")
        private String companyName;

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User.class)
        @JoinColumn(name = "user_id", referencedColumnName = "userId")
        private User user;

        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Product.class)
        @JoinTable(name = "supplier_product", joinColumns = {
                        @JoinColumn(name = "supplier_id", referencedColumnName = "supplierId") }, inverseJoinColumns = {
                                        @JoinColumn(name = "product_id", referencedColumnName = "productId") })
        private List<Product> products;

        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Order.class)
        @JoinTable(name = "supplier_order", joinColumns = {
                        @JoinColumn(name = "supplier_id", referencedColumnName = "supplierId") }, inverseJoinColumns = {
                                        @JoinColumn(name = "order_id", referencedColumnName = "orderId") })
        private List<Order> orders;
}
