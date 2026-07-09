package com.badran.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * Spring Boot entry point for the Badran Car Wash Supplies modular monolith.
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class BadranStoreApplication {

    /**
     * Starts the Badran Store backend application.
     */
    public static void main(String[] args) {
        SpringApplication.run(BadranStoreApplication.class, args);
    }
}
