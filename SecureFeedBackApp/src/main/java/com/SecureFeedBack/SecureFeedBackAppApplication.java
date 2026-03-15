package com.SecureFeedBack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.SecureFeedBack.Model.User;
import com.SecureFeedBack.repository.UserRepository;

@SpringBootApplication
public class SecureFeedBackAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureFeedBackAppApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.boot.CommandLineRunner createAdmin(UserRepository repo,
                                                                   BCryptPasswordEncoder encoder){

        return args -> {

            if(repo.findByUsername("admin").isEmpty()){

                User admin = new User();

                admin.setUsername("CS4417");
                admin.setPassword(encoder.encode("saqibhakak!"));
                admin.setEmail("admin@test.com");
                admin.setFirstName("System");
                admin.setLastName("Admin");

                admin.setRole("ROLE_ADMIN");

                repo.save(admin);
            }
        };
    }
}