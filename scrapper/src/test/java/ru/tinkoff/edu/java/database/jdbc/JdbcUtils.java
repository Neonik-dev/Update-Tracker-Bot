package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class JdbcUtils {
    private static final String SELECT_CHATS_BY_ID_QUERY = "SELECT count(*) FROM chats where id = ?";
    private static final String SELECT_DOMAINS_BY_ID_QUERY = "SELECT count(*) FROM domains where id = ?";
    private static final String SELECT_DOMAINS_BY_NAME_QUERY = "SELECT count(*) FROM domains where name = ?";
    private static final String SELECT_LINKS_BY_LINK_QUERY = "SELECT count(*) FROM links where link = ?";
    private static final String SELECT_LINKS_BY_ID_QUERY = "SELECT count(*) FROM links where id = ?";
    private static final String SELECT_CHAT_LINK_BY_CHAT_LINK_ID_QUERY = "SELECT count(*) FROM chat_link where chat_id = ? AND link_id = ?";
    private final String LINK = "http://noNameSiteForTest.com/no-name-site-for-test";
    public final String DOMAIN_NAME = "noNameSiteForTest";
    private final JdbcTemplate jdbcTemplate;
    public ChatData createChatData() {
        long chatId;
        do {
            chatId = new Random().nextLong();
        } while (!checkMissingDataChat(chatId));

        return ChatData.builder().id(chatId).build();
    }

    public DomainData createDomainData() {
        long domainId;
        do {
            domainId = new Random().nextLong();
        } while (!checkMissingDataDomain(domainId));

        DomainData domainData = DomainData.builder()
                .name(DOMAIN_NAME)
                .id(domainId)
                .build();
        return domainData;
    }

    public LinkData createLinkData() {
        long linkId;
        do {
            linkId = new Random().nextLong();
        } while (!checkMissingDataLink(linkId));

        LinkData linkData = new LinkData();
        linkData.setId(linkId);
        linkData.setLink(LINK);
        linkData.setDataChanges(Map.of("commits", "15", "comments", "11"));
        linkData.setDomainId(createDomainData().getId());
        return linkData;
    }

    public boolean checkMissingDataChat(Long chatId) {
        Integer result = jdbcTemplate.queryForObject(SELECT_CHATS_BY_ID_QUERY, Integer.class, chatId);
        return result == null || result == 0;
    }

    public boolean checkMissingDataDomain(Long domainId) {
        Integer result = jdbcTemplate.queryForObject(SELECT_DOMAINS_BY_ID_QUERY, Integer.class, domainId);
        return result == null || result == 0;
    }

    public boolean checkMissingDataDomain(String name) {
        Integer result = jdbcTemplate.queryForObject(SELECT_DOMAINS_BY_NAME_QUERY, Integer.class, name);
        return result == null || result == 0;
    }

    public boolean checkMissingDataLink(String link) {
        Integer result = jdbcTemplate.queryForObject(SELECT_LINKS_BY_LINK_QUERY, Integer.class, link);
        return result == null || result == 0;
    }

    public boolean checkMissingDataLink(long linkId) {
        Integer result = jdbcTemplate.queryForObject(SELECT_LINKS_BY_ID_QUERY, Integer.class, linkId);
        return result == null || result == 0;
    }

    public boolean checkMissingDataChatLink(long chatId, long linkId) {
        Integer result = jdbcTemplate.queryForObject(
                SELECT_CHAT_LINK_BY_CHAT_LINK_ID_QUERY,
                Integer.class,
                chatId,
                linkId
        );
        return result == null || result == 0;
    }
}
