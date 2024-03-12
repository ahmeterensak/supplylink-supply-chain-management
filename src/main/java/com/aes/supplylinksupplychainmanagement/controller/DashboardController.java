package com.aes.supplylinksupplychainmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aes.supplylinksupplychainmanagement.enums.OrderStatus;
import com.aes.supplylinksupplychainmanagement.model.Order;
import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.service.IOrderService;
import com.aes.supplylinksupplychainmanagement.service.IProductService;
import com.aes.supplylinksupplychainmanagement.service.ISupplierService;
import com.aes.supplylinksupplychainmanagement.service.IUserService;
import com.aes.supplylinksupplychainmanagement.util.SlugCreator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/supplier")
@AllArgsConstructor
public class DashboardController {

    IProductService productService;
    IUserService userService;
    ISupplierService supplierService;
    IOrderService orderService;

    /**
     * Displays the dashboard page with relevant information.
     *
     * @param model            The model object to add attributes for rendering the
     *                         view.
     * @param authentication   The authentication object representing the current
     *                         user's authentication details.
     * @param session          The HttpSession object to retrieve session-specific
     *                         data.
     * @param productCreated   A flag indicating whether a product was recently
     *                         created.
     * @param focusProductList A flag indicating whether to focus on the product
     *                         list.
     * @return The view name to render the dashboard.
     */
    @GetMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session,
            @RequestParam(value = "productCreated", required = false) String productCreated,
            @RequestParam(value = "focusProductList", required = false) String focusProductList) {

        addDashboardModelAttributes(model, session);

        if (productCreated != null) {
            model.addAttribute("productCreatedMessage", true);
        }

        if (focusProductList != null) {
            model.addAttribute("focusProductList", true);
        }

        return "dashboard.html";
    }

    /**
     * Retrieves the list of products and adds them to the model.
     *
     * @param model   The model object to add attributes for rendering the view.
     * @param session The HttpSession object to retrieve session-specific data.
     * @return The view name to render the dashboard.
     */
    @GetMapping("/getProductList")
    public String getProductList(Model model, HttpSession session) {

        User user = getUserFromSession(session);

        List<Product> products = productService.findProductListByUserId(user.getUserId());

        model.addAttribute("products", products);
        return "dashboard.html";
    }

    /**
     * Creates a new product.
     *
     * @param model   The model object to add attributes for rendering the view.
     * @param product The product object to be created.
     * @param errors  The Errors object containing binding/validation errors, if
     *                any.
     * @param session The HttpSession object to retrieve session-specific data.
     * @return The redirect URL to the dashboard page with appropriate flags.
     */
    @PostMapping("/createProduct")
    public String createProduct(Model model, @Valid @ModelAttribute("product") Product product, Errors errors,
            HttpSession session) {
        if (errors.hasErrors()) {
            return "redirect:/supplier/dashboard";
        }
        User user = getUserFromSession(session);

        product.setSlug(SlugCreator.createSlug(product.getName()));

        Supplier supplier = supplierService.findSupplierWithProductsByUserId(user.getUserId());
        Product createdProduct = productService.createProduct(product);

        if (createdProduct != null) {
            supplier.getProducts().add(createdProduct);
            supplierService.update(supplier);
        }
        return "redirect:/supplier/dashboard?productCreated=true";
    }

    // Update Product

    /**
     * Updates an existing product.
     *
     * @param product The product object containing the updated information.
     * @param errors  The Errors object containing binding/validation errors, if
     *                any.
     * @return The redirect URL to the dashboard page with appropriate flags.
     */
    @PostMapping("/updateProduct")
    public String updateProduct(@Valid @ModelAttribute("product") Product product, Errors errors) {
        if (errors.hasErrors())
            return "redirect:/supplier/dashboard?focusProductList=true";
        productService.updateProduct(product);

        return "redirect:/supplier/dashboard?focusProductList=true";
    }

    // Delete Product

    /**
     * Deletes a product.
     *
     * @param product The product object to be deleted.
     * @return The view name to render the dashboard.
     */
    @PostMapping("/deleteProduct")
    public String deleteProduct(@Valid @ModelAttribute("product") Product product) {

        productService.deleteProduct(product);
        return "dashboard.html";
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId     The ID of the order to be updated.
     * @param orderStatus The new status of the order.
     * @param session     The HttpSession object to retrieve session-specific data.
     * @return The redirect URL to the dashboard page.
     */
    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") int orderId,
            @RequestParam("orderStatus") OrderStatus orderStatus, HttpSession session) {

        User user = getUserFromSession(session);
        Supplier supplier = supplierService.findSupplierByUserId(user.getUserId());
        boolean isSuccessful = orderService.updateOrderStatus(orderId, supplier.getSupplierId(), orderStatus);
        if (!isSuccessful) {
            // todo error handling
        }
        return "redirect:/supplier/dashboard";

    }

    /**
     * Adds attributes to the model for rendering the dashboard view.
     *
     * @param model   The model object to add attributes for rendering the view.
     * @param session The HttpSession object to retrieve session-specific data.
     */
    private void addDashboardModelAttributes(Model model, HttpSession session) {

        /* Get user info from current session */
        User user = getUserFromSession(session);

        /* Get and add owned product list and single product entity to model */
        List<Product> products = productService.findProductListByUserId(user.getUserId());
        model.addAttribute("product", new Product());
        model.addAttribute("products", products);

        /* Get and add logged-in supplier to model */
        Supplier supplier = supplierService.findSupplierByUserId(user.getUserId());
        model.addAttribute("supplier", supplier);

        /* Add Order Status types to model */
        model.addAttribute("orderStatusTypeList", OrderStatus.values());

        /* Get all orders */
        Map<OrderStatus, List<Order>> orderMap = supplierService.findOrdersBySupplierId(supplier.getSupplierId());

        /* Get and add active orders to model */
        List<Order> activeOrders = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            if (status == OrderStatus.DELIVERED)
                continue;
            if (orderMap.get(status) != null)
                activeOrders.addAll(orderMap.get(status));
        }
        model.addAttribute("activeOrders", activeOrders);

        /* Get and add completed orders to model */
        List<Order> completedOrders = new ArrayList<>();
        if (orderMap.get(OrderStatus.DELIVERED) != null)
            completedOrders.addAll(orderMap.get(OrderStatus.DELIVERED));
        model.addAttribute("completedOrders", completedOrders);

    }

    /**
     * Retrieves the user from the session.
     *
     * @param session The HttpSession object to retrieve session-specific data.
     * @return The User object retrieved from the session.
     */
    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

}
