package ru.tinkoff.edu.java.bot.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.clients.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.clients.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.clients.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.configuration.ScrapperConfig;

@RequiredArgsConstructor
public class ScrapperClient {
    private final WebClient webClient;

    public ScrapperClient(ScrapperConfig scrapperConfig) {
        webClient = WebClient.builder().baseUrl(ScrapperConfig.getBaseUrl()).build();
    }

    public ListLinksResponse getListLinks(Long chatId) {
        return webClient.get()
                .uri("links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve().bodyToMono(ListLinksResponse.class).block();
    }

    public AddLinkRequest postLink(Long chatId) {
        return webClient.post()
                .uri("links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve().bodyToMono(AddLinkRequest.class).block();
    }

    public RemoveLinkRequest deleteLink(Long chatId) {
        return webClient.delete()
                .uri("links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve().bodyToMono(RemoveLinkRequest.class).block();
    }

    public void postChatId(Long chatId) {
        webClient.post().uri("tg-chat/{chatId}", chatId).retrieve().bodyToMono(RemoveLinkRequest.class).block();
    }

    public void deleteChatId(Long chatId) {
        webClient.delete().uri("tg-chat/{chatId}", chatId).retrieve().bodyToMono(RemoveLinkRequest.class).block();
    }
}
