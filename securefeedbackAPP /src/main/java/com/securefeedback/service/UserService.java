package com.securefeedback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.securefeedback.Model.User;
import com.securefeedback.dao.UserDao;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isStrongPassword(String password) {
        if (password == null) {
            return false;
        }

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-={}\\[\\]:;\"'<>,./~`|\\\\]).{8,}$");
    }

    public User registerUser(String firstName, String lastName, String username,
                             String password, String email,
                             String securityQuestion1, String securityAnswer1,
                             String securityQuestion2, String securityAnswer2) {

        if (userDao.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userDao.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        if (!isStrongPassword(password)) {
            throw new RuntimeException("Password must be strong: at least 8 characters, with uppercase, lowercase, number, and special character.");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("ROLE_USER");
        user.setSecurityQuestion1(securityQuestion1);
        user.setSecurityAnswer1(securityAnswer1.trim().toLowerCase());
        user.setSecurityQuestion2(securityQuestion2);
        user.setSecurityAnswer2(securityAnswer2.trim().toLowerCase());

        return userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public List<User> getLockedUsers() {
        return userDao.findLockedUsers();
    }

    public void unlockUser(Long userId) {
        User user = userDao.findById(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        userDao.unlockAccount(user);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!isStrongPassword(newPassword)) {
            throw new RuntimeException("New password must be strong: at least 8 characters, with uppercase, lowercase, number, and special character.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }

    public boolean verifySecurityAnswers(String username,
                                         String question1, String answer1,
                                         String question2, String answer2) {

        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if ("ROLE_ADMIN".equals(user.getRole())) {
            return false;
        }

        return user.getSecurityQuestion1() != null
                && user.getSecurityQuestion2() != null
                && user.getSecurityAnswer1() != null
                && user.getSecurityAnswer2() != null
                && user.getSecurityQuestion1().equals(question1)
                && user.getSecurityAnswer1().equals(answer1.trim().toLowerCase())
                && user.getSecurityQuestion2().equals(question2)
                && user.getSecurityAnswer2().equals(answer2.trim().toLowerCase());
    }

    public void resetForgottenPassword(String username, String newPassword) {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Admin cannot reset password using forgot password.");
        }

        if (!isStrongPassword(newPassword)) {
            throw new RuntimeException("New password must be strong: at least 8 characters, with uppercase, lowercase, number, and special character.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setAccountLocked(false);
        user.setFailedAttempts(0);
        userDao.save(user);
    }

    public void handleFailedLogin(String username) {
        User user = userDao.findByUsername(username);

        if (user != null && !user.getAccountLocked()) {
            userDao.incrementFailedAttempts(user);
        }
    }

    public void handleSuccessfulLogin(String username) {
        User user = userDao.findByUsername(username);

        if (user != null && user.getFailedAttempts() > 0) {
            userDao.resetFailedAttempts(user);
        }
    }
}