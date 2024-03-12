package com.aes.supplylinksupplychainmanagement.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.aes.supplylinksupplychainmanagement.model.Admin;
import com.aes.supplylinksupplychainmanagement.model.Customer;
import com.aes.supplylinksupplychainmanagement.model.Supplier;
import com.aes.supplylinksupplychainmanagement.model.User;
import com.aes.supplylinksupplychainmanagement.repository.UserRepository;
import com.aes.supplylinksupplychainmanagement.service.ICustomerService;
import com.aes.supplylinksupplychainmanagement.service.ISupplierService;
import com.aes.supplylinksupplychainmanagement.service.IUserService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private ICustomerService customerService;
    private ISupplierService supplierService;
    private IUserService userService;
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticates user based on provided credentials.
     *
     * @param authentication The authentication object.
     * @return The authentication token.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials!"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(user);
            return new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
        } else {
            throw new BadCredentialsException("Invalid credentials!");
        }
    }

    /**
     * Retrieves granted authorities for the authenticated user.
     *
     * @param user The authenticated user.
     * @return The list of granted authorities.
     */
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Customer customer = customerService.findByUserId(user.getUserId());
        if (customer != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            return grantedAuthorities;
        }

        Supplier supplier = supplierService.findSupplierByUserId(user.getUserId());
        if (supplier != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SUPPLIER"));
            return grantedAuthorities;
        }

        Admin admin = userService.findAdminByUserId(user.getUserId());
        if (admin != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + admin.getPosition()));
            return grantedAuthorities;
        }

        return null;
    }

    /**
     * Indicates whether this AuthenticationProvider supports the specified
     * authentication token class.
     *
     * @param authentication The authentication token class.
     * @return True if supported, otherwise false.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}