package com.aes.supplylinksupplychainmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((request) -> request
                .requestMatchers("/", "/home").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/productDetails").permitAll()
                .requestMatchers("/{supplierSlug}/{slug}").permitAll()
                .requestMatchers("/addItemToCart").hasRole("CUSTOMER")
                .requestMatchers("/submitReview").hasRole("CUSTOMER")
                .requestMatchers("/updateQuantity").hasRole("CUSTOMER")
                .requestMatchers("/deleteCartItem").hasRole("CUSTOMER")
                .requestMatchers("/createOrder").hasRole("CUSTOMER")
                .requestMatchers("/cart").hasRole("CUSTOMER")
                .requestMatchers("/myOrders").hasRole("CUSTOMER")
                .requestMatchers("/supplier/**").hasRole("SUPPLIER"))
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/home", true).failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}