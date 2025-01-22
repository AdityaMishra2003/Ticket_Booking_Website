package com.example.travelagency.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travelagency.Dto.UserDto;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Service.EmailService;
import com.example.travelagency.Service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;
    private EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model, HttpSession session) {
        User existingUser = userService.findByUsername(userDto.getUsername());
        if (existingUser != null && existingUser.isVerified()) {
            model.addAttribute("Userexist", existingUser);
            return "register";
        }

        // Generate OTP and send email without saving the user
        String otp = userService.generateOtp();
        try {
            userService.sendVerificationEmail(userDto.getUsername(), otp);
        } catch (MessagingException e) {
            e.printStackTrace();
            model.addAttribute("emailError", "Failed to send verification email. Please try again.");
            return "register";
        }
        
        // Store UserDto in session
        session.setAttribute("userDto", userDto);
        return "redirect:/verify-otp?username=" + userDto.getUsername();
    }

    @GetMapping("/verify-otp")
    public String verifyOtp(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("username") String username, @RequestParam("otp") String otp, Model model, HttpSession session) {
        boolean isOtpValid = userService.verifyOtp(username, otp);
        if (isOtpValid) {
            UserDto userDto = (UserDto) session.getAttribute("userDto");
            if (userDto != null) {
                User user = new User();
                user.setUsername(userDto.getUsername());
                user.setPassword(userDto.getPassword());
                user.setFullname(userDto.getFullname());
                user.setVerified(true);
                userService.save(user);
                session.removeAttribute("userDto"); // Clean up session
                return "redirect:/register?success";
            } else {
                model.addAttribute("otpError", "User session has expired. Please register again.");
                return "register";
            }
        } else {
            model.addAttribute("username", username);
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "verify-otp";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam("username") String username, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            String otp = userService.generateOtp();
            try {
                userService.sendVerificationEmail(username, otp);
            } catch (MessagingException e) {
                e.printStackTrace();
                model.addAttribute("emailError", "Failed to send verification email. Please try again.");
                return "forgot-password";
            }
            return "redirect:/verify-otp-forgot?username=" + username;
        } else {
            model.addAttribute("usernameError", "Username not found.");
            return "forgot-password";
        }
    }

    @GetMapping("/verify-otp-forgot")
    public String verifyOtpForgot(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "verify-otp-forgot";
    }

    @PostMapping("/verify-otp-forgot")
    public String verifyOtpForgot(@RequestParam("username") String username, @RequestParam("otp") String otp, Model model) {
        boolean isOtpValid = userService.verifyOtp(username, otp);
        if (isOtpValid) {
            return "redirect:/change-password?username=" + username;
        } else {
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "verify-otp-forgot";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePasswordSubmit(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setPassword(password); // Ensure to encode the password before saving
            userService.save(user);
            return "redirect:/login?passwordChanged";
        } else {
            model.addAttribute("error", "Username not found.");
            return "change-password";
        }
    }

    @GetMapping("/welcome")
    public String welcome(Model model, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            System.out.println("OAuth2 email: " + email); // Debug
            if (email != null) {
                User user = userService.findByUsername(email);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "welcome";
                } else {
                    model.addAttribute("errorMessage", "User not found.");
                }
            } else {
                model.addAttribute("errorMessage", "OAuth2 email attribute is null.");
            }
        } else {
            if (principal != null) {
                String username = principal.getName();
                System.out.println(username);
                User user = userService.findByUsername(username);
                model.addAttribute("user", user);
            }
            return "welcome";
        }
        return "error";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContactForm(@RequestParam String name, @RequestParam String email,
                                    @RequestParam String phone, @RequestParam String messageInfo, Model model) {
        String subject = "Contact Request From " + email;
        String body = "Received The Contact Request From \n" + "Name: " + name + "\nEmail: " + email +
                      "\nPhone: " + phone + "\n" + messageInfo;
        emailService.sendContactInfo(email, subject, body);
        model.addAttribute("successMessage", "Message sent successfully!");
        return "contact";
    }

    @GetMapping("/about")
    public String getAboutPage(Model model, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            System.out.println("OAuth2 email: " + email); // Debug
            if (email != null) {
                User user = userService.findByUsername(email);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "about";
                } else {
                    model.addAttribute("errorMessage", "User not found.");
                }
            } else {
                model.addAttribute("errorMessage", "OAuth2 email attribute is null.");
            }
        } else {
            if (principal != null) {
                String username = principal.getName();
                System.out.println(username);
                User user = userService.findByUsername(username);
                model.addAttribute("user", user);
            }
            return "about";
        }
        return "error";
    }
}
