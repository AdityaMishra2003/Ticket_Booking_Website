package com.example.taskmanagement.demo.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.taskmanagement.demo.repository.UserRepository;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;

        // Check if the authentication principal is an OAuth2 user
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            String username = userDetails.getAttribute("email") != null 
                    ? userDetails.getAttribute("email")
                    : userDetails.getAttribute("login") + "@gmail.com";

            // Check if the username matches the admin email
            if ("am9982061@gmail.com".equals(username)) {
                redirectUrl = "/AdminDashboard"; // Redirect to Admin Dashboard
            } else if (userRepo.findByUsername(username) != null) {
                redirectUrl = "/UserDashboard"; // Redirect to User Dashboard
            } else {
                // User not found, set an error message
                request.getSession().setAttribute("errorMessage", "Please register first.");
                redirectUrl = "/login"; // Redirect to the login page
            }

        } else {
            // For regular username/password authentication
            String username = authentication.getName();
            String fullName = userRepo.findByUsername(username).getFullname();
            System.out.println(fullName);

            if ("Admin".equals(fullName) && "am9982061@gmail.com".equals(username)) {
                redirectUrl = "/AdminDashboard"; // Redirect to Admin Dashboard
            } else {
                redirectUrl = "/UserDashboard"; // Redirect to User Dashboard
            }
        }

        // Perform the redirect
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
