package com.aes.supplylinksupplychainmanagement.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Cart {

    Map<String, Integer> cartItems;
    int total;

    public Cart() {
        cartItems = new HashMap<String, Integer>();
    }
}
