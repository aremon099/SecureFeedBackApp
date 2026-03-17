package com.securefeedback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.securefeedback.Model.Feedback;
import com.securefeedback.dao.FeedbackDao;

import jakarta.validation.Valid;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackDao feedbackDao;

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }

    @PostMapping("/feedback")
    public String submitFeedback(
            @Valid @ModelAttribute Feedback feedback,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "feedback";
        }

        feedbackDao.save(feedback);
        model.addAttribute("success", "Thank you for your feedback!");
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }
}