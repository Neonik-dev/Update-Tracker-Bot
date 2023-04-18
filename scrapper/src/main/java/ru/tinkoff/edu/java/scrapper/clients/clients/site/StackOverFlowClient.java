package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.StackOverflowParseResponse;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverFlowConfig;

import java.util.Map;

public class StackOverFlowClient implements BaseSiteClient {
    private final WebClient webClient;

    public StackOverFlowClient(StackOverFlowConfig stackOverFlowConfig) {
        webClient = WebClient.builder().baseUrl(stackOverFlowConfig.getBaseUrl()).build();
    }

    @Override
    public Map<String, String> getUpdates(BaseParseResponse response) {
//        StackOverflowParseResponse stackResponse = (StackOverflowParseResponse) response;
//        return webClient
//                .get()
//                .uri("questions/{id}?site=stackoverflow", stackResponse.questionId())
//                .retrieve()
//                .bodyToMono(StackOverFlowSiteResponse.class)
//                .block()
//                .questions()[0];
        return null;
    }
}
