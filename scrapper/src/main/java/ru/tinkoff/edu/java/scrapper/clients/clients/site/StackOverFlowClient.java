package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.StackOverflowParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.StackOverFlowSiteResponse;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverFlowConfig;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class StackOverFlowClient implements BaseSiteClient {
    private final WebClient webClient;

    public StackOverFlowClient(StackOverFlowConfig stackOverFlowConfig) {
        webClient = WebClient.builder().baseUrl(stackOverFlowConfig.getBaseUrl()).build();
    }

    @Override
    public Map<String, String> getUpdates(BaseParseResponse response) {
        return new HashMap<>();
    }

    @Override
    public OffsetDateTime getUpdatedDate(BaseParseResponse response) {
        StackOverflowParseResponse stackResponse = (StackOverflowParseResponse) response;
        return webClient
                .get()
                .uri("questions/{id}?site=stackoverflow", stackResponse.questionId())
                .retrieve()
                .bodyToMono(StackOverFlowSiteResponse.class)
                .block()
                .questions()[0]
                .updatedDate()
                .withOffsetSameLocal(ZoneOffset.ofHours(3));
    }
}
