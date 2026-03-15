package com.SecureFeedBack.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.SecureFeedBack.Model.Feedback;
import com.SecureFeedBack.repository.FeedbackRepository;

import jakarta.validation.Valid;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }

    @PostMapping("/feedback")
    public String submitFeedback(@Valid @ModelAttribute("feedback") Feedback feedback,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "feedback";
        }

        feedback.setSubmittedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);

        model.addAttribute("success", "Thank you for your feedback!");
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }
}