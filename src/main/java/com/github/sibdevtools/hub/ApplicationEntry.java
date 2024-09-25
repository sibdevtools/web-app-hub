package com.github.sibdevtools.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author sibmaks
 * @since 0.0.1
 */

@EnableJpaRepositories(basePackages = "com.github.sibdevtools")
@EntityScan(basePackages = "com.github.sibdevtools")
@SpringBootApplication(scanBasePackages = {
        "com.github.sibdevtools.hub.config",
        "com.github.sibdevtools"
})
public class ApplicationEntry {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntry.class, args);
    }
}
