package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.GitHubParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubSiteResponse;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfig;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GitHubClient implements BaseSiteClient {
    private final WebClient webClient;
    private static final String MAIN_INFO_LINK = "repos/{user}/{repo}";
    private static final String GET_COMMITS_LINK = MAIN_INFO_LINK + "/commits";
    private static final String GET_BRANCHES_LINK = MAIN_INFO_LINK + "/branches";
    private static final String GET_ISSUES_EVENTS_LINK = MAIN_INFO_LINK + "/issues/events";
    private static final String GET_ISSUES_COMMENTS_LINK = MAIN_INFO_LINK + "/issues/comments";
    private static final String GET_PULL_REQUESTS_LINK = MAIN_INFO_LINK + "/pulls";
    private String user;
    private String repo;

    public GitHubClient(GitHubConfig gitHubConfig) {
        webClient = WebClient.builder().baseUrl(gitHubConfig.getBaseUrl()).build();
    }

    @Override
    public Map<String, String> getUpdates(BaseParseResponse response) {
        user = ((GitHubParseResponse) response).user();
        repo = ((GitHubParseResponse) response).repo();

        Map<String, String> dataUpdates = new HashMap<>();
        dataUpdates.put("commits", getFieldLength(GET_COMMITS_LINK));
        dataUpdates.put("branches", getFieldLength(GET_BRANCHES_LINK));
        dataUpdates.put("issues", getFieldLength(GET_ISSUES_EVENTS_LINK));
        dataUpdates.put("issues_comments", getFieldLength(GET_ISSUES_COMMENTS_LINK));
        dataUpdates.put("pull_requests", getFieldLength(GET_PULL_REQUESTS_LINK));
        return dataUpdates;
    }
    @Override
    public OffsetDateTime getUpdatedDate(BaseParseResponse response) {
        GitHubParseResponse clearResponse = (GitHubParseResponse) response;

        return webClient
                .get()
                .uri(MAIN_INFO_LINK, clearResponse.user(), clearResponse.repo())
                .retrieve()
                .bodyToMono(GitHubSiteResponse.class)
                .block()
                .updatedDate()
                .withOffsetSameLocal(ZoneOffset.ofHours(3));
    }

    private String getFieldLength(String url) {
        return String.valueOf(
                Objects.requireNonNull(
                        webClient
                                .get()
                                .uri(url, user, repo)
                                .retrieve()
                                .bodyToMono(Object[].class)
                                .block()
                ).length
        );
    }
}
