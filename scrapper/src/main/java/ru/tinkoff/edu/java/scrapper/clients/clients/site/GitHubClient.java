package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.GitHubParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubSiteResponse;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class GitHubClient implements BaseSiteClient {
    private final WebClient webClient;
    private static final String MAIN_INFO_LINK = "repos/{user}/{repo}";
    private static final String GET_COMMITS_LINK = "repositories/%d/commits";

    public GitHubClient(GitHubConfig gitHubConfig) {
        webClient = WebClient.builder().baseUrl(gitHubConfig.getBaseUrl()).build();
    }

    @Override
    public Map<String, String> getUpdates(BaseParseResponse response) {
        Map<String, String> dataUpdates = new HashMap<>();
        GitHubParseResponse githubResponse = (GitHubParseResponse) response;
        GitHubSiteResponse siteResponse = webClient
                .get()
                .uri(MAIN_INFO_LINK, githubResponse.user(), githubResponse.repo())
                .retrieve()
                .bodyToMono(GitHubSiteResponse.class)
                .block(); // запрос на получение id и последнего обновления

        dataUpdates.put("updated_date", siteResponse.updatedDate().toString());
        dataUpdates.put("commits", getCommits(siteResponse.id()).toString());
        return dataUpdates;
    }

    private Integer getCommits(long id) {
        return Objects.requireNonNull(webClient
                .get()
                .uri(String.format(GET_COMMITS_LINK, id))
                .retrieve()
                .bodyToMono(Object[].class)
                .block()).length; // забираю кол-во коммитов
    }
}
