package com.trang.estore.common;

import com.trang.estore.users.Role;
import com.trang.estore.users.User;
import com.trang.estore.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("minhtrang03@gmail.com").isEmpty()) {
                User user = new User();
                user.setName("trang");
                user.setEmail("minhtrang03@gmail.com");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRole(Role.USER);
                userRepository.save(user);
            }
        };
    }
}
