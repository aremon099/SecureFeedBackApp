package com.SecureFeedBack.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SecureFeedBack.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }

            userService.registerUser(firstName, lastName, email, username, password);
            model.addAttribute("success", "Account created successfully! Please login.");
            return "login";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "dashboard";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal,
                                 Model model) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "New passwords do not match");
                return "change-password";
            }

            userService.changePassword(principal.getName(), newPassword);
            return "redirect:/login?logout";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "change-password";
        }
    }
}