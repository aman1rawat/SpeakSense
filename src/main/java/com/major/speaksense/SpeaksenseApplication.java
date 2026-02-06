package com.major.speaksense;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpeaksenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeaksenseApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public ChatModel chatLanguageModel(@Value("${GEMINI_API_KEY}") String apiKey) {
		return GoogleAiGeminiChatModel.builder()
				.apiKey(apiKey)
				.modelName("gemini-2.5-flash-lite")
				.logRequests(true)
				.logResponses(true)
				.build();
	}

}
