package ru.tinkoff.edu.java.scrapper.clients.clients;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.StackOverFlowQuestionsResponse;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverFlowConfig;

public class StackOverFlowClient {
    private final WebClient webClient;

    public StackOverFlowClient(StackOverFlowConfig stackOverFlowConfig) {
        webClient = WebClient.builder().baseUrl(stackOverFlowConfig.getBaseUrl()).build();
    }

    public BaseResponse getInformation(long id) {
        return webClient.get().uri("questions/{id}?site=stackoverflow", id).retrieve().bodyToMono(StackOverFlowQuestionsResponse.class).block().questions()[0];
    }
}
