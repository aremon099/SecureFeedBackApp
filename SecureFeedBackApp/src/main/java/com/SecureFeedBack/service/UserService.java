package com.SecureFeedBack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SecureFeedBack.Model.User;
import com.SecureFeedBack.repository.UserRepository;

@Service
public class UserService{

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 15 * 60 * 1000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(String firstName, String lastName, String email, String username, String password) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        validateUsername(username);
        validatePasswordStrength(password);

        String cleanFirstName = firstName.trim();
        String cleanLastName = lastName.trim();
        String cleanEmail = email.trim().toLowerCase();
        String cleanUsername = username.trim();

        if (userRepository.existsByUsername(cleanUsername)) {
            throw new RuntimeException("Registration failed. Please try different credentials.");
        }

        if (userRepository.existsByEmail(cleanEmail)) {
            throw new RuntimeException("Registration failed. Please try different credentials.");
        }

        User user = new User();
        user.setFirstName(cleanFirstName);
        user.setLastName(cleanLastName);
        user.setEmail(cleanEmail);
        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    public void registerAdmin(String firstName, String lastName, String email, String username, String password) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        validateUsername(username);
        validatePasswordStrength(password);

        String cleanFirstName = firstName.trim();
        String cleanLastName = lastName.trim();
        String cleanEmail = email.trim().toLowerCase();
        String cleanUsername = username.trim();

        if (userRepository.existsByUsername(cleanUsername)) {
            throw new RuntimeException("Registration failed. Please try different credentials.");
        }

        if (userRepository.existsByEmail(cleanEmail)) {
            throw new RuntimeException("Registration failed. Please try different credentials.");
        }

        User user = new User();
        user.setFirstName(cleanFirstName);
        user.setLastName(cleanLastName);
        user.setEmail(cleanEmail);
        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }

    public void changePassword(String username, String newPassword) {
        validatePasswordStrength(newPassword);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Password change failed"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newFailAttempts);

        if (newFailAttempts >= MAX_ATTEMPTS) {
            user.setLockTime(System.currentTimeMillis());
        }

        userRepository.save(user);
    }

    public void resetFailedAttempts(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }
    }

    public boolean isAccountLocked(User user) {
        if (user.getLockTime() == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long lockTimeMillis = user.getLockTime();

        if (currentTime - lockTimeMillis > LOCK_TIME_DURATION) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            return false;
        }

        return true;
    }

    public List<User> getLockedUsers() {
        return userRepository.findByFailedAttemptsGreaterThanEqual(MAX_ATTEMPTS);
    }

    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFailedAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
    }

    private void validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(fieldName + " is required");
        }

        String trimmed = value.trim();
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            throw new RuntimeException(fieldName + " must be 2-50 characters");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        String trimmed = email.trim();
        if (trimmed.length() > 100) {
            throw new RuntimeException("Email must not exceed 100 characters");
        }

        if (!trimmed.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Invalid email format");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }

        String trimmed = username.trim();
        if (trimmed.length() < 3 || trimmed.length() > 30) {
            throw new RuntimeException("Username must be 3-30 characters");
        }

        if (!trimmed.matches("^[A-Za-z0-9_]+$")) {
            throw new RuntimeException("Username can only contain letters, numbers, and underscore");
        }
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new RuntimeException("Password must contain at least one number");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new RuntimeException("Password must contain at least one special character");
        }

        if (password.length() > 128) {
            throw new RuntimeException("Password must not exceed 128 characters");
        }
    }
}