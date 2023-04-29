package ru.tinkoff.edu.java.scrapper.clients.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.configuration.TgBotConfig;

@RequiredArgsConstructor
public class TgBotClient {
    private final WebClient webClient;

    public TgBotClient(TgBotConfig tgBotConfig) {
        webClient = WebClient.builder().baseUrl(tgBotConfig.getBaseUrl()).build();
    }

    public void postUpdates(LinkUpdateRequest linkUpdateRequest) {
        webClient.post().uri("updates")
                .body(Mono.just(linkUpdateRequest), LinkUpdateRequest.class)
                .retrieve().bodyToMono(Void.class).block();
    }
}
