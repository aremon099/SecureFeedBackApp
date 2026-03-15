package com.SecureFeedBack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureFeedBack.Model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
