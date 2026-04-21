package com.aboutme.agent.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class to load environment variables from .env file
 */
@Configuration
@Slf4j
public class EnvLoader {

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMissing()
                    .load();

            // Set environment variables from .env file to system properties
            dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            log.error("Note: .env file not found or could not be read. Using system environment variables.");
        }
    }
}

