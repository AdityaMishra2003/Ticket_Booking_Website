package com.example.travelagency.config;

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

import com.example.travelagency.Dto.UserDto;
import com.example.travelagency.Repository.UserRepository;
import com.example.travelagency.Service.UserService;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            
            // Print all attributes for debugging
            System.out.println("User attributes: " + userDetails.getAttributes());

            String username = userDetails.getAttribute("email") != null 
                    ? userDetails.getAttribute("email")
                    : userDetails.getAttribute("login") + "@gmail.com";

			   // Fetch the full name from Google OAuth2 attributes
            if (userRepo.findByUsername(username) == null) {
                UserDto user = new UserDto();
                user.setUsername(username);
                
                // Fetch the full name from Google OAuth2 attributes
                String fullName = userDetails.getAttribute("name"); // Google key for full name
                System.out.println("Fullname:"+userDetails.getAttribute("name"));
                user.setFullname(fullName);
                user.setPassword("Dummy"); // Set a dummy password
                
                userService.save(user);
            }
        }
        redirectUrl = "/welcome";
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
