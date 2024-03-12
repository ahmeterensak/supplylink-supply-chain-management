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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
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
public class Product extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private int productId;

        @NotBlank(message = "Name cannot be blank")
        private String name;

        @NotBlank(message = "Description cannot be blank")
        private String description;

        @Min(value = 1, message = "Price must be greater than 0")
        private int price;

        @Min(value = 1, message = "Stock quantity must be greater than 0")
        private int stockQuantity;

        private String slug;

        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.PERSIST,
                        CascadeType.REFRESH })
        private List<Review> reviews;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH }, targetEntity = Supplier.class)
        @JoinTable(name = "supplier_product", joinColumns = {
                        @JoinColumn(name = "product_id", referencedColumnName = "productId") }, inverseJoinColumns = {
                                        @JoinColumn(name = "supplier_id", referencedColumnName = "supplierId") })
        private Supplier supplier;
}
