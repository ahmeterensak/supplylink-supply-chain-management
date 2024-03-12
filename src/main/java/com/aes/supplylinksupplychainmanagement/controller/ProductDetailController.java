package com.aes.supplylinksupplychainmanagement.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aes.supplylinksupplychainmanagement.model.Cart;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.Review;
import com.aes.supplylinksupplychainmanagement.service.IProductService;
import com.aes.supplylinksupplychainmanagement.service.IReviewService;
import com.aes.supplylinksupplychainmanagement.util.SlugCreator;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ProductDetailController {

    private final IProductService productService;
    private final IReviewService reviewService;

    /**
     * Displays the details of a specific product.
     * 
     * @param pathVariableSlug The slug obtained from the path variable.
     * @param paramSlug        The slug obtained from the request parameter.
     * @param model            The model object to add attributes for rendering the
     *                         view.
     * @param session          The HttpSession object to store and retrieve data
     *                         related to the session.
     * @return The view name to render the product details page.
     */
    @GetMapping("/{supplierSlug}/{slug}")
    public String displayProductDetails(
            @PathVariable(value = "slug", required = false) String pathVariableSlug,
            @RequestParam(value = "slug", required = false) String paramSlug, Model model, HttpSession session) {
        String slug = pathVariableSlug != null ? pathVariableSlug : paramSlug;
        Product product = productService.findBySlug(slug);
        if (product == null)
            return "redirect:/home";
        List<Review> reviews = reviewService.findAllByProductId(product.getProductId());
        product.setReviews(reviews);
        int reviewCount = reviewService.findCountByProductId(product.getProductId());
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cartItemCount", cart.getCartItems().size());
        model.addAttribute("product", product);
        model.addAttribute("reviewCount", reviewCount);
        return "product_details.html";
    }

    /**
     * Adds an item to the user's cart.
     * 
     * @param slug               The slug of the product to be added to the cart.
     * @param supplierName       The name of the supplier associated with the
     *                           product.
     * @param quantity           The quantity of the product to be added.
     * @param redirectToHome     Indicates whether to redirect to the home page
     *                           after adding the item to the cart.
     * @param session            The HttpSession object to store and retrieve data
     *                           related to the session.
     * @param redirectAttributes The RedirectAttributes object to add attributes for
     *                           redirection.
     * @return The redirect URL after adding the item to the cart.
     */
    @PostMapping("/addItemToCart")
    public String addItemToCart(@RequestParam String slug, @RequestParam String supplierName,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam(value = "redirectToHome", required = false) String redirectToHome, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        if (cart.getCartItems().containsKey(slug)) {
            int dataQuantity = cart.getCartItems().get(slug);
            cart.getCartItems().put(slug, dataQuantity + quantity);
        } else {
            cart.getCartItems().put(slug, quantity);
        }
        session.setAttribute("cart", cart);
        if (redirectToHome != null)
            return "redirect:/home";
        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("supplierSlug", SlugCreator.createSlug(supplierName));
        return "redirect:/{supplierSlug}/{slug}";
    }
}
