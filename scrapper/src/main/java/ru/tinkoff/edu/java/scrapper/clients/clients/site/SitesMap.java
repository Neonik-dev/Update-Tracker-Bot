package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.InvalidLinkException;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SitesMap {
    private static final String LINK_ERROR_MESSAGE = "Бот пока не может отслеживать ссылку с таким доменным именем";
    private static final Map<String, BaseSiteClient> SITES = new HashMap<>();
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;

    @PostConstruct
    public void initMap() {
        SITES.put("github.com", gitHubClient);
        SITES.put("stackoverflow.com", stackOverFlowClient);
    }

    public BaseSiteClient getClient(String domain) {
        BaseSiteClient client = SITES.get(domain);
        if (client == null) {
            throw new InvalidLinkException(LINK_ERROR_MESSAGE);
        }
        return client;
    }
}
