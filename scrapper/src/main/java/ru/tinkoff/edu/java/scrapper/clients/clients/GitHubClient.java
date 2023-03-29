package ru.tinkoff.edu.java.scrapper.clients.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfig;

@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(GitHubConfig gitHubConfig) {
        webClient = WebClient.builder().baseUrl(gitHubConfig.getBaseUrl()).build();
    }

    public BaseResponse getInformation(String user, String repo) {
        return webClient.get().uri("repos/{user}/{repo}", user, repo).retrieve().bodyToMono(GitHubResponse.class).block();
    }
}
