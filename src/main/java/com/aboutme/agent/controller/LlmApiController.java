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
    private final OpenAIClient geminiAIClient;
    private final OpenAIClient groqAIClient;

    @Override
    public ResponseEntity<QuestionResponse> askQuestion(QuestionRequest questionRequest) {
        ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                .addUserMessage(questionRequest.getQuestion());

        // Set model based on provider
        switch (questionRequest.getAiProvider()) {
            case GEMINI -> paramsBuilder.model("gemini-2.5-flash");
            case OPENAI -> paramsBuilder.model(ChatModel.GPT_5_2);
            case GROQ -> paramsBuilder.model("openai/gpt-oss-120b");
        }

        ChatCompletionCreateParams params = paramsBuilder.build();

        // Use async API to get CompletableFuture<ChatCompletion>
        CompletableFuture<ChatCompletion> completionFuture = null;
        switch (questionRequest.getAiProvider()){
            case GEMINI -> completionFuture = CompletableFuture.supplyAsync(() -> geminiAIClient.chat().completions().create(params));
            case OPENAI -> completionFuture = CompletableFuture.supplyAsync(() -> openAIClient.chat().completions().create(params));
            case GROQ -> completionFuture = CompletableFuture.supplyAsync(() -> groqAIClient.chat().completions().create(params));
        }


        return completionFuture.thenApply(chatCompletion -> {
            QuestionResponse response = new QuestionResponse();
            var firstChoice = chatCompletion.choices().getFirst();
            String answer = firstChoice.message().content().orElse("");
            response.setAnswer(answer);
            boolean validated = firstChoice.message().isValid();
            response.setValidated(validated);

            // Compute a simple confidence score:
            // - if the message is marked valid, give a high confidence (0.95)
            // - otherwise a medium confidence (0.5)
            // This is a conservative fallback when token log-probs are not available.
            float confidence = validated ? 0.95f : 0.5f;
            response.setConfidenceScore(confidence);

            return ResponseEntity.ok(response);
        }).join();
    }
}
