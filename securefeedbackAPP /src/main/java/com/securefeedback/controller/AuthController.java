package com.securefeedback.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.securefeedback.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String email,
            @RequestParam String securityQuestion1,
            @RequestParam String securityAnswer1,
            @RequestParam String securityQuestion2,
            @RequestParam String securityAnswer2,
            Model model) {

        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }

            userService.registerUser(firstName, lastName, username, password, email,
                    securityQuestion1, securityAnswer1,
                    securityQuestion2, securityAnswer2);

            model.addAttribute("success", "Registration successful! Please login.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "dashboard";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String verifySecurity(
            @RequestParam String username,
            @RequestParam String securityQuestion1,
            @RequestParam String securityAnswer1,
            @RequestParam String securityQuestion2,
            @RequestParam String securityAnswer2,
            Model model) {

        try {
            boolean valid = userService.verifySecurityAnswers(
                    username,
                    securityQuestion1, securityAnswer1,
                    securityQuestion2, securityAnswer2
            );

            if (!valid) {
                model.addAttribute("error", "Incorrect answers or admin cannot use this feature.");
                return "forgot-password";
            }

            model.addAttribute("username", username);
            return "reset-password";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String username,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        try {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                model.addAttribute("username", username);
                return "reset-password";
            }

            userService.resetForgottenPassword(username, newPassword);

            model.addAttribute("success", "Password reset successful! Please login.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "reset-password";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            @SessionAttribute(value = "username", required = false) String username,
            Principal principal) {

        try {
            String actualUsername = username;

            if (actualUsername == null && principal != null) {
                actualUsername = principal.getName();
            }

            if (actualUsername == null) {
                model.addAttribute("error", "User session not found");
                return "change-password";
            }

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "change-password";
            }

            userService.changePassword(actualUsername, currentPassword, newPassword);

            model.addAttribute("success", "Password updated successfully!");
            return "change-password";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "change-password";
        }
    }
}