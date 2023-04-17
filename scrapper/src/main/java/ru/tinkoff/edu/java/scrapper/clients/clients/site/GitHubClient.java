package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.GitHubParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseSiteResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubSiteResponse;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfig;

@RequiredArgsConstructor
public class GitHubClient implements BaseSiteClient {
    private final WebClient webClient;

    public GitHubClient(GitHubConfig gitHubConfig) {
        webClient = WebClient.builder().baseUrl(gitHubConfig.getBaseUrl()).build();
    }

    @Override
    public BaseSiteResponse getUpdates(BaseParseResponse response) {
        GitHubParseResponse githubResponse = (GitHubParseResponse) response;
        return webClient
                .get()
                .uri("repos/{user}/{repo}", githubResponse.user(), githubResponse.repo())
                .retrieve()
                .bodyToMono(GitHubSiteResponse.class)
                .block();
    }
}
