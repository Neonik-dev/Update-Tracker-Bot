package ru.tinkoff.edu.java.scrapper.configuration.db.service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
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
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfiguration {
    private final DSLContext dslContext;

    @Bean
    public ChatLinkRepository chatLinkRepository() {
        return new JooqChatLinkRepository(dslContext);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JooqChatRepository(dslContext);
    }

    @Bean
    public DomainRepository domainRepository() {
        return new JooqDomainRepository(dslContext);
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JooqLinkRepository(dslContext);
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
