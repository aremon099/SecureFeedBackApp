package com.securefeedback.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securefeedback.dao.FeedbackDao;
import com.securefeedback.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackDao feedbackDao;

    @GetMapping
    public String adminDashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("lockedUsers", userService.getLockedUsers());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("feedbackList", feedbackDao.findAll());
        return "admin";
    }

    @PostMapping("/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        return "redirect:/admin";
    }
}