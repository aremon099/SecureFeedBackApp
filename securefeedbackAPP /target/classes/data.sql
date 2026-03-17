
INSERT IGNORE INTO users (
    first_name, last_name, username, password, email, role, account_locked, failed_attempts,
    security_question_1, security_answer_1, security_question_2, security_answer_2
)
VALUES (
    'Test', 'User', 'testuser', 'test123', 'testuser@gmail.com', 'ROLE_USER', FALSE, 0,
    'What is your favourite color?', 'blue',
    'What is your pet''s name?', 'tommy'
);

INSERT IGNORE INTO users (
    first_name, last_name, username, password, email, role, account_locked, failed_attempts,
    security_question_1, security_answer_1, security_question_2, security_answer_2
)
VALUES (
    'Admin', 'User', 'admin', 'admin123', 'admin@gmail.com', 'ROLE_ADMIN', FALSE, 0,
    NULL, NULL, NULL, NULL
);

-- FEEDBACK

INSERT IGNORE INTO feedback (id, user_id, name, email, message, submitted_at)
VALUES
(1, 1, 'Saqib Hakak', 'saqib.hakak@gmail.com', 'Great secure application!', CURRENT_TIMESTAMP),
(2, 2, 'Jarin Tasmin', 'jarin.tasmin@gmail.com', 'Love the design and security features.', CURRENT_TIMESTAMP);