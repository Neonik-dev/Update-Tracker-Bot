package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SitesMap {
    private static final Map<String, BaseSiteClient> SITES = new HashMap<>();
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;

    @PostConstruct
    public void initMap() {
        SITES.put("github.com", gitHubClient);
        SITES.put("stackoverflow.com", stackOverFlowClient);
    }

    public BaseSiteClient getClient(String domain) {
        return SITES.get(domain);
    }
}
