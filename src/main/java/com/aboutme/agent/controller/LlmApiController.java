package com.aboutme.agent.controller;

import com.example.api.LlmApi;
import com.example.model.QuestionRequest;
import com.example.model.QuestionResponse;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@RequiredArgsConstructor
@RestController
public class LlmApiController implements LlmApi {

    private final OpenAIClient openAIClient;

    @Override
    public ResponseEntity<QuestionResponse> askQuestion(QuestionRequest questionRequest) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(questionRequest.getQuestion())
                .model(ChatModel.GPT_5_2)
                .build();

        // Use async API to get CompletableFuture<ChatCompletion>
        CompletableFuture<ChatCompletion> chatCompletionFuture =
            CompletableFuture.supplyAsync(() ->
                openAIClient.chat().completions().create(params)
            );

        return chatCompletionFuture.thenApply(chatCompletion -> {
            QuestionResponse response = new QuestionResponse();
            response.setAnswer(chatCompletion.choices().getFirst().message().content().orElse(""));
            //TODO: to validate
            response.setValidated(false);
            //TODO : compute confidence score
            response.setConfidenceScore(0.95f);
            return ResponseEntity.ok(response);
        }).join();
    }
}
