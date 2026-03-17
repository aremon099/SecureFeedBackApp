package com.securefeedback.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.securefeedback.Model.Feedback;
import com.securefeedback.repository.FeedbackRepository;

@Component
public class FeedbackDao {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback findById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAllByOrderBySubmittedAtDesc();
    }

    public List<Feedback> findByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }
}