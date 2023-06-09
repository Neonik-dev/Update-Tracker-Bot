package ru.tinkoff.edu.java.scrapper.configuration.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public ChatLinkRepository chatLinkRepository() {
        return new JdbcChatLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public DomainRepository domainRepository() {
        return new JdbcDomainRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatLinkService chatLinkService(ChatLinkRepository chatLinkRepository) {
        return new JdbcChatLinkService(chatLinkRepository);
    }

    @Bean
    public ChatService chatService(ChatRepository chatRepository) {
        return new JdbcChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
            DomainRepository domainRepository,
            ChatLinkRepository chatLinkRepository,
            LinkRepository linkRepository,
            ChatLinkService chatLinkService,
            SitesMap sitesMap
    ) {
        return new JdbcLinkService(domainRepository, chatLinkRepository, linkRepository, chatLinkService, sitesMap);
    }
}
