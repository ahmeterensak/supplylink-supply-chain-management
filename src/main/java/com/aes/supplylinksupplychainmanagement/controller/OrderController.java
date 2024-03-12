package com.aes.supplylinksupplychainmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aes.supplylinksupplychainmanagement.model.Cart;
import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.OrderDetail;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.Review;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.service.ICustomerService;
import com.aes.supplylinksupplychainmanagement.service.IOrderDetailService;
import com.aes.supplylinksupplychainmanagement.service.IOrderService;
import com.aes.supplylinksupplychainmanagement.service.IProductService;
import com.aes.supplylinksupplychainmanagement.service.IReviewService;
import com.aes.supplylinksupplychainmanagement.service.ISupplierService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IOrderDetailService orderDetailService;
    private final IProductService productService;
    private final ISupplierService supplierService;
    private final ICustomerService customerService;
    private final IReviewService reviewService;

    /**
     * Displays the orders page for the logged-in user.
     * 
     * @param model   The model object to add attributes for rendering the view.
     * @param session The HttpSession object to retrieve the logged-in user.
     * @return The view name to render the orders page.
     */
    @GetMapping("/myOrders")
    public String displayOrdersPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.findWithReviewsAndProductsByUserId(user.getUserId());

        List<Integer> reviewedProducts = customer.getReviews().stream()
                .map(review -> review.getProduct().getProductId())
                .collect(Collectors.toList());

        model.addAttribute("orderMap", orderService.getOrderMap(user));
        model.addAttribute("reviewedProducts", reviewedProducts);
        return "orders.html";
    }

    /**
     * Creates a new order based on the products in the user's cart.
     * 
     * @param model   The model object to add attributes for rendering the view.
     * @param session The HttpSession object to retrieve the user's cart and
     *                products.
     * @return Redirects to the orders page after creating the order.
     */
    @PostMapping("/createOrder")
    public String createOrder(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Cart cart = (Cart) session.getAttribute("cart");

        Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cartProducts");

        if (cart == null)
            return "cart.html";

        Order order = orderService.createOrder(user, products);
        List<OrderDetail> orderDetails = orderDetailService.createOrderDetails(cart.getCartItems(), order);

        supplierService.setOrdersToSuppliers(order, orderDetails);

        session.removeAttribute("cart");
        session.removeAttribute("cartProducts");

        return "redirect:/myOrders";
    }

    /**
     * Submits a review for a product.
     * 
     * @param productId The ID of the product being reviewed.
     * @param rating    The rating given to the product.
     * @param comment   The comment or review text.
     * @param session   The HttpSession object to retrieve the logged-in user.
     * @return Redirects to the orders page after submitting the review.
     */
    @PostMapping("/submitReview")
    public String submitReview(@RequestParam int productId, @RequestParam int rating, @RequestParam String comment,
            HttpSession session) {
        Product product = productService.findById(productId);
        User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.findByUserId(user.getUserId());

        if (product == null || customer == null)
            return "redirect:/cart";

        Review review = new Review(comment, rating, LocalDate.now(), product, customer);
        reviewService.save(review);

        return "redirect:/myOrders?reviewSubmit=true";
    }
}
