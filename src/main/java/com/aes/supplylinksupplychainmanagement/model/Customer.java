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
public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private int customerId;

        @NotBlank(message = "First Name cannot be blank")
        private String firstName;

        @NotBlank(message = "Last Name cannot be blank")
        private String lastName;

        @NotBlank(message = "Phone number cannot be blank")
        private String phone;

        @NotBlank(message = "Address number cannot be blank")
        private String address;

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User.class)
        @JoinColumn(name = "user_id", referencedColumnName = "userId")
        private User user;

        @OneToMany(mappedBy = "customer", cascade = { CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.PERSIST,
                        CascadeType.REFRESH })
        private List<Review> reviews;
}
