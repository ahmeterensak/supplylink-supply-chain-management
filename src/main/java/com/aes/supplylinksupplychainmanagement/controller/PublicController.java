package com.aes.supplylinksupplychainmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.service.ICustomerService;
import com.aes.supplylinksupplychainmanagement.service.ISupplierService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {

    private final ICustomerService customerService;
    private final ISupplierService supplierService;

    /**
     * Displays the registration page for customers and suppliers.
     * 
     * @param model The model object to add attributes for rendering the view.
     * @return The view name to render the registration page.
     */
    @GetMapping("/register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("user", new User());
        return "register.html";
    }

    /**
     * Creates a new customer.
     * 
     * @param customer The customer object containing registration information.
     * @param errors   The Errors object for validation errors.
     * @return The view name to render after the registration process.
     */
    @PostMapping("/createCustomer")
    public String createCustomer(@Valid @ModelAttribute("customer") Customer customer, Errors errors) {
        if (errors.hasErrors())
            return "register.html";
        boolean isSaved = customerService.createCustomer(customer);
        if (!isSaved)
            return "register.html";
        return "redirect:/login?register=true";
    }

    /**
     * Creates a new supplier.
     * 
     * @param supplier The supplier object containing registration information.
     * @param errors   The Errors object for validation errors.
     * @return The view name to render after the registration process.
     */
    @PostMapping("/createSupplier")
    public String createSupplier(@Valid @ModelAttribute("supplier") Supplier supplier, Errors errors) {
        if (errors.hasErrors())
            return "register.html";
        boolean isSaved = supplierService.createSupplier(supplier);
        if (!isSaved)
            return "register.html";
        return "redirect:/login?register=true";
    }

}
