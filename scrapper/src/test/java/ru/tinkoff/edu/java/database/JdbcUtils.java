package ru.tinkoff.edu.java.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.ConvertorFromMapToJson;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
@RequiredArgsConstructor
public class JdbcUtils {
    private static final String SELECT_CHATS_BY_ID_QUERY = "SELECT count(*) FROM chats where id = ?";
    private static final String SELECT_DOMAINS_BY_ID_QUERY = "SELECT count(*) FROM domains where id = ?";
    private static final String SELECT_DOMAINS_BY_NAME_QUERY = "SELECT count(*) FROM domains where name = ?";
    private static final String SELECT_LINKS_BY_LINK_QUERY = "SELECT count(*) FROM links where link = ?";
    private static final String SELECT_LINKS_BY_ID_QUERY = "SELECT count(*) FROM links where id = ?";
    private static final String SELECT_CHAT_LINK_BY_CHAT_LINK_ID_QUERY = "SELECT count(*) FROM chat_link where chat_id = ? AND link_id = ?";
    private final static String INSERT_DOMAINS_QUERY = "INSERT INTO domains(id, name) VALUES (?, ?)";
    private final static String INSERT_CHATS_QUERY = "INSERT INTO chats(id) VALUES (?)";
    private final static String INSERT_LINKS_QUERY = "INSERT INTO links(id, link, domain_id, data_changes) VALUES (?, ?, ?, ?)";
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

        return DomainData.builder()
                .name(DOMAIN_NAME)
                .id(domainId)
                .build();
    }

    public LinkData createLinkData() {
        long linkId;
        do {
            linkId = new Random().nextLong();
        } while (!checkMissingDataLink(linkId));

        return LinkData.builder()
                .id(linkId)
                .link(LINK)
                .dataChanges(Map.of("commits", "15", "comments", "11"))
                .pageUpdateDate(OffsetDateTime.now())
                .domainId(createDomainData().getId())
                .build();
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

    public void assertDomainResult(DomainData result, DomainData domainData) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getName(), domainData.getName()),
                () -> assertEquals(result.getCreatedDate(), LocalDate.now())
        );
    }

    public void assertChatResult(ChatData result, ChatData chatData) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getId(), chatData.getId()),
                () -> assertEquals(result.getLastCallDate(), LocalDate.now())
        );
    }

    public void assertLinkResult(LinkData result, LinkData linkData) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getLink(), linkData.getLink()),
                () -> assertEquals(result.getDomainId(), linkData.getDomainId()),
                () -> assertEquals(result.getDataChanges().get("commits"), "15"),
                () -> assertEquals(result.getDataChanges().get("comments"), "11")
        );
    }

    public void assertChatLinkResult(ChatLinkData result, ChatLinkData chatLinkData) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getChatId(), chatLinkData.getChatId()),
                () -> assertEquals(result.getLinkId(), chatLinkData.getLinkId())
        );
    }

    public void initEnvironmentChatLink(DomainData domainData, ChatData chatData, LinkData linkData) {
        jdbcTemplate.update(INSERT_DOMAINS_QUERY, domainData.getId(), domainData.getName());
        jdbcTemplate.update(INSERT_CHATS_QUERY, chatData.getId());
        jdbcTemplate.update(INSERT_LINKS_QUERY,
                linkData.getId(),
                linkData.getLink(),
                linkData.getDomainId(),
                new ConvertorFromMapToJson().convertToDatabaseColumn(linkData.getDataChanges())
        );
    }
}
