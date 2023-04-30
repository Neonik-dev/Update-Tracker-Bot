package ru.tinkoff.edu.java.scrapper.clients.clients.notifier;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.configuration.TgBotConfig;

public class BotClient implements SenderUpdatedLinks {
    private final WebClient webClient;

    public BotClient(TgBotConfig tgBotConfig) {
        webClient = WebClient.builder().baseUrl(tgBotConfig.getBaseUrl()).build();
    }

    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        webClient.post().uri("updates")
                .body(Mono.just(linkUpdateRequest), LinkUpdateRequest.class)
                .retrieve().bodyToMono(Void.class).block();
    }
}
