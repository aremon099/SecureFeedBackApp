package com.SecureFeedBack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.SecureFeedBack.Model.User;
import com.SecureFeedBack.repository.UserRepository;
import com.SecureFeedBack.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/dashboard", "/feedback", "/change-password").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    @Component
    public static class AuthenticationSuccessListener
            implements ApplicationListener<AuthenticationSuccessEvent> {

        @Autowired
        private UserService userService;

        @Override
        public void onApplicationEvent(AuthenticationSuccessEvent event) {
            String username = event.getAuthentication().getName();
            userService.resetFailedAttempts(username);
        }
    }

    @Component
    public static class AuthenticationFailureListener
            implements ApplicationListener<AbstractAuthenticationFailureEvent> {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserService userService;

        @Override
        public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
            String username = event.getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user != null) {
                userService.increaseFailedAttempts(user);
            }
        }
    }
}