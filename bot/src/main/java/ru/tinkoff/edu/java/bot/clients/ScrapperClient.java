package ru.tinkoff.edu.java.bot.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.scrapper.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.scrapper.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.scrapper.ListLinksResponse;
import ru.tinkoff.edu.java.bot.dto.scrapper.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.configuration.ScrapperConfiguration;

import java.net.URI;
import java.util.function.Function;

@Slf4j
public class ScrapperClient {
    private static final String URL_LINK = "links";
    private static final String URL_CHAT = "tg-chat/{chatId}";
    private static final String CHAT_HEADER = "Tg-Chat-Id";
    private static final Function<ClientResponse, Mono<? extends Throwable>> ERROR_HEADER = error -> {
        log.error("Scrapper вернул неудовлетворительный ответ");
        return null;
    };
    private final WebClient webClient;

    public ScrapperClient(ScrapperConfiguration scrapperConfiguration) {
        webClient = WebClient.builder().baseUrl(scrapperConfiguration.getBaseUrl()).build();
    }

    public ListLinksResponse getListLinks(Long chatId) {
        return webClient.get()
                .uri(URL_LINK)
                .header(CHAT_HEADER, String.valueOf(chatId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        ERROR_HEADER
                ).bodyToMono(ListLinksResponse.class).block();
    }

    public void postLink(Long chatId, URI uri) {
        webClient.post()
                .uri(URL_LINK)
                .header(CHAT_HEADER, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new AddLinkRequest(uri)))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        ERROR_HEADER
                ).bodyToMono(AddLinkRequest.class).block();
    }

    public void deleteLink(Long chatId, URI uri) {
        webClient.method(HttpMethod.DELETE)
                .uri(URL_LINK)
                .header(CHAT_HEADER, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new AddLinkRequest(uri)))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        ERROR_HEADER
                ).bodyToMono(LinkResponse.class).block();
    }

    public void postChatId(Long chatId) {
        webClient.post().uri(URL_CHAT, chatId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        ERROR_HEADER
                ).toBodilessEntity().block();
    }

    public void deleteChatId(Long chatId) {
        webClient.delete().uri(URL_CHAT, chatId).retrieve().bodyToMono(RemoveLinkRequest.class).block();
    }
}
