package com.aboutme.agent.controller;

import com.example.model.QuestionRequest;
import com.example.model.QuestionResponse;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@ExtendWith(MockitoExtension.class)
class LlmApiControllerTest {

	@Test
	void askQuestion_openai_returnsMappedResponse() {
		OpenAIClient openAI = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);
		OpenAIClient gemini = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);
		OpenAIClient groq = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);

		ChatCompletion mockCompletion = mock(ChatCompletion.class, RETURNS_DEEP_STUBS);
		when(mockCompletion.choices().getFirst().message().content()).thenReturn(Optional.of("Hello from OpenAI"));
		when(mockCompletion.choices().getFirst().message().isValid()).thenReturn(true);

		when(openAI.chat().completions().create(any(com.openai.models.chat.completions.ChatCompletionCreateParams.class))).thenReturn(mockCompletion);

		LlmApiController controller = new LlmApiController(openAI, gemini, groq);

		QuestionRequest req = new QuestionRequest();
		req.setQuestion("Say hi");
		req.setAiProvider(QuestionRequest.AiProviderEnum.OPENAI);

		var respEntity = controller.askQuestion(req);
		assertNotNull(respEntity);
		QuestionResponse body = respEntity.getBody();
		assertNotNull(body);
		assertEquals("Hello from OpenAI", body.getAnswer());
		assertTrue(body.getValidated());
		assertEquals(0.95f, body.getConfidenceScore());
	}

	@Test
	void askQuestion_gemini_returnsMappedResponse() {
		OpenAIClient openAI = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);
		OpenAIClient gemini = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);
		OpenAIClient groq = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);

		ChatCompletion mockCompletion = mock(ChatCompletion.class, RETURNS_DEEP_STUBS);
		when(mockCompletion.choices().getFirst().message().content()).thenReturn(Optional.of("Hello from Gemini"));
		when(mockCompletion.choices().getFirst().message().isValid()).thenReturn(false);

		when(gemini.chat().completions().create(any(com.openai.models.chat.completions.ChatCompletionCreateParams.class))).thenReturn(mockCompletion);

		LlmApiController controller = new LlmApiController(openAI, gemini, groq);

		QuestionRequest req = new QuestionRequest();
		req.setQuestion("Say hi");
		req.setAiProvider(QuestionRequest.AiProviderEnum.GEMINI);

		var respEntity = controller.askQuestion(req);
		assertNotNull(respEntity);
		QuestionResponse body = respEntity.getBody();
		assertNotNull(body);
		assertEquals("Hello from Gemini", body.getAnswer());
		assertFalse(body.getValidated());
		assertEquals(0.5f, body.getConfidenceScore());
	}
}
