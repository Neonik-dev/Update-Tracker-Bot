package ru.tinkoff.edu.java.scrapper.clients.clients.site.enums;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.BaseSiteClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.GitHubClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.StackOverFlowClient;

public enum SiteEnum {
    GITHUB("github.com"),
    STACKOVERFLOW("stackoverflow.com");

    private final String domain;
    private BaseSiteClient client;

    SiteEnum(String name) {
        this.domain = name;
    }

    @Component
    @RequiredArgsConstructor
    public static class BaseSiteClientInjector {
        private final GitHubClient gitHubClient;
        private final StackOverFlowClient stackOverFlowClient;

        @PostConstruct
        public void postConstruct() {
            GITHUB.client = gitHubClient;
            STACKOVERFLOW.client = stackOverFlowClient;
        }
    }

    public BaseSiteClient getClient() {
        return client;
    }

    public String getDomain() {
        return domain;
    }
}
