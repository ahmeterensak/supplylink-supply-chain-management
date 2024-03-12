package com.aes.supplylinksupplychainmanagement.model;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_review")
public class Review {

        public Review(String comment,
                        @Min(value = 1, message = "Rating must be greater than 0") @Max(value = 5, message = "Rating must be lower than 5") int rating,
                        LocalDate date, Product product, Customer customer) {
                this.comment = comment;
                this.rating = rating;
                this.date = date;
                this.product = product;
                this.customer = customer;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private int reviewId;

        private String comment;

        @Min(value = 1, message = "Rating must be greater than 0")
        @Max(value = 5, message = "Rating must be lower than 5")
        private int rating;

        private LocalDate date;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH, }, targetEntity = Product.class)
        @JoinColumn(name = "product_id", referencedColumnName = "productId")
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH, }, targetEntity = Customer.class)
        @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
        private Customer customer;
}
