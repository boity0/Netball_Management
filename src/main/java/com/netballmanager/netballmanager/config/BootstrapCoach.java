package com.netballmanager.netballmanager.config;

import com.netballmanager.netballmanager.entity.NetballUser;
import com.netballmanager.netballmanager.entity.UserRole;
import com.netballmanager.netballmanager.repository.NetballUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapCoach {
    @Bean
    CommandLineRunner createBootstrapCoach(NetballUserRepository users, PasswordEncoder encoder,
                                           @Value("${app.bootstrap-coach.email:}") String email,
                                           @Value("${app.bootstrap-coach.password:}") String password) {
        return args -> {
            if (email.isBlank() && password.isBlank()) {
                return;
            }
            if (email.isBlank() || password.length() < 10) {
                throw new IllegalStateException(
                        "Set both BOOTSTRAP_COACH_EMAIL and BOOTSTRAP_COACH_PASSWORD (at least 10 characters)");
            }
            if (!users.existsByEmailIgnoreCase(email)) {
                users.save(new NetballUser(email.trim().toLowerCase(), encoder.encode(password), UserRole.COACH));
            }
        };
    }
}
