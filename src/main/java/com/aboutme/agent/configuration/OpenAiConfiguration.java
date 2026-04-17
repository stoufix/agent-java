package com.aboutme.agent.configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class OpenAiConfiguration {

    @Bean
    public OpenAIClient openAIClient(
            @Value("${openai.apiKey}") final String apiKey,
            @Value("${openai.baseUrl:}") final Optional<String> baseUrl) {
        return OpenAIOkHttpClient.builder()
                // Configures using the `openai.apiKey`, `openai.orgId`, `openai.projectId`, `openai.webhookSecret` and `openai.baseUrl` system properties
                // Or configures using the `OPENAI_API_KEY`, `OPENAI_ORG_ID`, `OPENAI_PROJECT_ID`, `OPENAI_WEBHOOK_SECRET` and `OPENAI_BASE_URL` environment variables
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
    }
    @Bean
    public OpenAIClient groqAIClient(
            @Value("${groq.apiKey}") final String apiKey,
            @Value("${groq.baseUrl:}") final Optional<String> baseUrl) {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
    }
    @Bean
    public OpenAIClient geminiAIClient(
            @Value("${gemini.apiKey}") final String apiKey,
            @Value("${gemini.baseUrl:}") final Optional<String> baseUrl) {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
    }
}
