package com.aes.supplylinksupplychainmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import com.aes.supplylinksupplychainmanagement.model.Cart;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.service.ICustomerService;
import com.aes.supplylinksupplychainmanagement.service.IProductService;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class CartController {

    IProductService productService;
    ICustomerService customerService;

    final Logger logger = Logger.getLogger(CartController.class.getName());

    /**
     * Retrieves details of the items in the cart.
     * 
     * @param model   The model object to add attributes for rendering the view.
     * @param session The HttpSession object to retrieve the cart information.
     * @return The view name to render the cart details.
     */
    @GetMapping("/cart")
    public String getCartDetails(Model model, HttpSession session) {
        // Retrieve the cart from the session
        Cart cart = (Cart) session.getAttribute("cart");

        // If the cart is null, redirect to the home page
        if (cart == null) {
            return "redirect:/home";
        }

        // Retrieve the list of products in the cart
        List<Product> products = productService.findBySlugs(cart.getCartItems().keySet());
        Map<Product, Integer> productQuantityList = new HashMap<>();

        // Calculate the subtotal and shipping cost
        int subTotal = 0;
        int shippingCost = 5;

        // Iterate through the products in the cart and calculate the subtotal
        for (String slug : cart.getCartItems().keySet()) {
            for (Product product : products) {
                if (product.getSlug().equals(slug)) {
                    productQuantityList.put(product, cart.getCartItems().get(slug));
                    subTotal += product.getPrice() * productQuantityList.get(product);
                    break;
                }
            }
        }

        // Add attributes to the model for rendering the view
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("totalPrice", subTotal + shippingCost);
        model.addAttribute("cartProducts", productQuantityList);
        session.setAttribute("cartProducts", productQuantityList);

        return "cart.html";
    }

    /**
     * Updates the quantity of a cart item.
     * 
     * @param quantity The new quantity of the item.
     * @param slug     The slug of the product to update.
     * @param session  The HttpSession object to retrieve the cart information.
     * @return The redirect URL to the cart page.
     */
    @PostMapping("/updateQuantity")
    public String updateCartItemQuantity(@RequestParam int quantity, @RequestParam String slug,
            HttpSession session) {
        // Retrieve the cart from the session
        Cart cart = (Cart) session.getAttribute("cart");

        // Update the quantity of the item in the cart
        cart.getCartItems().put(slug, quantity);

        // Update the cart in the session
        session.setAttribute("cart", cart);

        // Redirect to the cart page
        return "redirect:/cart";
    }

    /**
     * Deletes a cart item.
     * 
     * @param slug    The slug of the product to delete.
     * @param session The HttpSession object to retrieve the cart information.
     * @return The redirect URL to the cart page.
     */
    @PostMapping("/deleteCartItem")
    public String deleteCartItem(@RequestParam String slug, HttpSession session) {
        // Retrieve the cart from the session
        Cart cart = (Cart) session.getAttribute("cart");

        // If the cart does not contain the specified item, return to the cart page
        if (!cart.getCartItems().containsKey(slug))
            return "cart.html";

        // Remove the item from the cart
        cart.getCartItems().remove(slug);

        // Redirect to the cart page
        return "redirect:/cart";
    }
}
