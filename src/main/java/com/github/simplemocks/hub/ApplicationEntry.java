package com.github.simplemocks.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@EnableJpaRepositories(
        basePackages = {
                "com.github.simple_mocks",
                "com.github.simplemocks"
        }
)
@EntityScan(
        basePackages = {
                "com.github.simple_mocks",
                "com.github.simplemocks"
        }
)
@SpringBootApplication(
        scanBasePackages = {
                "com.github.simplemocks.hub.config",
                "com.github.simplemocks",
                "com.github.simple_mocks",
        }
)
public class ApplicationEntry {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntry.class, args);
    }
}
