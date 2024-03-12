package com.aes.supplylinksupplychainmanagement.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.aes.supplylinksupplychainmanagement.model.Cart;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.service.IProductService;
import com.aes.supplylinksupplychainmanagement.service.IUserService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {

    private IProductService productService;
    private IUserService userService;

    /**
     * Displays the home page.
     * 
     * @param model          The model object to add attributes for rendering the
     *                       view.
     * @param authentication The authentication object containing details of the
     *                       logged-in user.
     * @param session        The HttpSession object to store user and cart
     *                       information.
     * @return The view name to render the home page.
     */
    @GetMapping(value = { "/", "/home" })
    public String displayHomePage(Model model, Authentication authentication, HttpSession session) {
        // Retrieve the user information based on the authenticated username
        User user = userService.findUserByEmail(authentication.getName());

        // Set the logged-in user information in the session
        session.setAttribute("loggedInUser", user);

        // Retrieve all products
        List<Product> products = productService.getAllProducts();

        // Retrieve the cart from the session
        Cart cart = (Cart) session.getAttribute("cart");

        // If the cart is null, create a new cart and set it in the session
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Add the count of items in the cart to the model
        model.addAttribute("cartItemCount", cart.getCartItems().size());

        // Add the list of products to the model
        model.addAttribute("products", products);

        // Render the home page
        return "home.html";
    }

}
