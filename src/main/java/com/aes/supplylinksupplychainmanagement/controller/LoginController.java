package com.aes.supplylinksupplychainmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    /**
     * Displays the login page.
     * 
     * @param error    Indicates if there was an error during login.
     * @param logout   Indicates if the user has logged out.
     * @param register Indicates if the user has registered successfully.
     * @param model    The model object to add error and info messages for rendering
     *                 the view.
     * @return The view name to render the login page.
     */
    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String displayLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "register", required = false) String register,
            Model model) {
        String errorMessage = null;
        String infoMessage = null;
        if (error != null)
            errorMessage = "Email or Password is incorrect.";
        else if (logout != null)
            infoMessage = "You have been successfully logged out.";
        else if (register != null)
            infoMessage = "You registered successfully. Login with registered credentials.";
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("infoMessage", infoMessage);
        return "login.html";
    }

    /**
     * Logs the user out and redirects to the login page.
     * 
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return Redirects to the login page with logout message.
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/login?logout=true";
    }

}
