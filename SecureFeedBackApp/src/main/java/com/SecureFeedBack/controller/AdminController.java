package com.SecureFeedBack.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.SecureFeedBack.repository.FeedbackRepository;
import com.SecureFeedBack.service.UserService;

@Controller
public class AdminController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("allFeedback", feedbackRepository.findAll());
        model.addAttribute("lockedUsers", userService.getLockedUsers());
        return "admin";
    }

    @PostMapping("/admin/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        return "redirect:/admin?unlocked";
    }
}