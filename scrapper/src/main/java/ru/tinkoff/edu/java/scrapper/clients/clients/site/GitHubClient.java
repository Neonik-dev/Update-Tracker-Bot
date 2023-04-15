package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfig;

@RequiredArgsConstructor
public class GitHubClient implements BaseSiteClient{
    private final WebClient webClient;

    public GitHubClient(GitHubConfig gitHubConfig) {
        webClient = WebClient.builder().baseUrl(gitHubConfig.getBaseUrl()).build();
    }

    @Override
    public BaseResponse getUpdates(String user, String repo) {
        return webClient.get().uri("repos/{user}/{repo}", user, repo).retrieve().bodyToMono(GitHubResponse.class).block();
    }
}
