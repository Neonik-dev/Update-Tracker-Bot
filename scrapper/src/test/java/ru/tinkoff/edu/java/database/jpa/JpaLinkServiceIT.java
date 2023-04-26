package ru.tinkoff.edu.java.database.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ScrapperApplication.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {Utils.class, DBConfiguration.class})
public class JpaLinkServiceIT {
    private final JpaChatService chatService;
    private final JpaLinkService linkService;
    private final JpaLinkRepository linkRepository;
    private final JpaDomainRepository domainRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Utils utils;
    private Chat chatData;
    private Link linkData;
    private final String LINK = "https://github.com/Neonik228/TinkoffProject/pull/10";
    private static final String SELECT_BY_LINK_QUERY = "SELECT id, link, page_updated_date, domain_id, data_changes FROM links WHERE link=?";
    private static final String SELECT_LINK_ID_QUERY = "SELECT id, link, page_updated_date, domain_id, data_changes FROM links WHERE id=?";
    private static final String SELECT_COINT_LINK_BY_ID_QUERY = "SELECT count(*) FROM links WHERE id=?";
    private static final String SELECT_CHAT_LINK_QUERY = "SELECT count(*) FROM chat_link WHERE chat_id=? AND link_id=?";
    private final RowMapper<Link> rowMapperLink = (rs, rowNum) -> new Link(
            rs.getLong(1),
            rs.getString(2),
            null,
            new Timestamp(rs.getTimestamp(3).getTime()).toLocalDateTime().atOffset(ZoneOffset.UTC),
            null,
            rs.getLong(4),
            new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(5))
    );

    @BeforeEach
    public void createLinkData() {
        chatData = utils.createChatData();
        linkData = utils.createLinkData();
    }

    @Test
    @Transactional
    @Rollback
    void addLink_OK() {
        // given
        chatService.register(chatData.getId());

        // when
        linkService.add(chatData.getId(), URI.create(LINK));
        linkRepository.flush();

        // then
        Link result = jdbcTemplate.queryForObject(SELECT_BY_LINK_QUERY, rowMapperLink, LINK);
        Integer countChatLink = jdbcTemplate.queryForObject(SELECT_CHAT_LINK_QUERY, Integer.class, chatData.getId(), result.getId());
        assertAll(
                () -> assertEquals(result.getLink(), LINK),
                () -> assertEquals(
                        result.getDomain().getId(),
                        domainRepository.findByName(URI.create(LINK).getHost()).getId()
                ),
                () -> assertEquals(countChatLink, 1)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeLink_OK() {
        // given
        chatService.register(chatData.getId());
        LinkResponse linkResponse = linkService.add(chatData.getId(), URI.create(LINK));
        linkRepository.flush();

        // then
        linkService.remove(chatData.getId(), URI.create(LINK));
        linkRepository.flush();

        // when
        Integer countLink = jdbcTemplate.queryForObject(SELECT_COINT_LINK_BY_ID_QUERY, Integer.class, linkResponse.id());
        Integer countChatLink = jdbcTemplate.queryForObject(SELECT_CHAT_LINK_QUERY, Integer.class, chatData.getId(), linkResponse.id());
        assertAll(
                () -> assertEquals(countChatLink, 0),
                () -> assertEquals(countLink, 0)
        );
    }

    @Test
    @Transactional
    @Rollback
    @Sql("/sql/fill_in links_table.sql")
    void getOldestUpdateLink_OK() {
        // given

        // when
        Optional<Link> result = linkService.getOldestUpdateLink();

        // then
        assertTrue(result.isPresent());
        assertAll(
                () -> assertEquals(result.get().getLink(), "https://nonamedomain.org/4"),
                () -> assertEquals(result.get().getSchedulerUpdateDate().toLocalDate(), LocalDate.now())
        );
    }

    @Test
    @Transactional
    @Rollback
    @Sql("/sql/fill_base_domains.sql")
    void updateDataChanges_OK() {
        // given
        linkData.setPageUpdatedDate(OffsetDateTime.of(2023, 1,1, 1, 1, 1, 1, ZoneOffset.UTC));
        linkData.setDomain(11L);
        Link saveLink = linkRepository.saveAndFlush(linkData);

        // when
        linkService.updateDataChanges(
                Map.of("commits", "12", "comments", "13"),
                OffsetDateTime.now(),
                saveLink.getId()
        );
        linkRepository.flush();

        // then
        Link result = jdbcTemplate.queryForObject(SELECT_LINK_ID_QUERY, rowMapperLink, saveLink.getId());
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getDataChanges().get("commits"), "12"),
                () -> assertEquals(result.getDataChanges().get("comments"), "13"),
                () -> assertEquals(result.getPageUpdatedDate().toLocalDate(), LocalDate.now())
        );
    }
}
