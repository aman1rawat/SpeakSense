package com.major.speaksense;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@SpringBootApplication
@EnableAsync
public class SpeaksenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeaksenseApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
				.build();

		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 90000)
				.responseTimeout(Duration.ofSeconds(90));

		return WebClient.builder()
				.exchangeStrategies(strategies)
				.clientConnector(new ReactorClientHttpConnector(httpClient));
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
