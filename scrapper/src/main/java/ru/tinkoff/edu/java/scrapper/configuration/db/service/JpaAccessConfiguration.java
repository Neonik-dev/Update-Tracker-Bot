package ru.tinkoff.edu.java.scrapper.configuration.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@RequiredArgsConstructor
public class JpaAccessConfiguration {
    private final JpaChatLinkRepository chatLinkRepository;
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaDomainRepository domainRepository;
    private final SitesMap sitesMap;

    @Bean
    public ChatLinkService chatLinkService() {
        return new JpaChatLinkService(chatLinkRepository);
    }

    @Bean
    public ChatService chatService() {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public LinkService linkService() {
        return new JpaLinkService(linkRepository, domainRepository, chatLinkRepository, chatRepository, sitesMap);
    }
}
