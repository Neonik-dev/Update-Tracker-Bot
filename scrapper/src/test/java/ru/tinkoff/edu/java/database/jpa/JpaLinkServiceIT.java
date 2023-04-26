package ru.tinkoff.edu.java.database.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
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
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;
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
import java.util.Date;
import java.util.HashMap;
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
    private static final String INSERT_LINK_QUERY = "INSERT INTO links(link, domain_id, data_changes) VALUES (?, ?, ?)";
    private static final ConvertorFromMapToJson CONVERTOR = new ConvertorFromMapToJson();
    private final RowMapper<Link> rowMapperLink = (rs, rowNum) -> new Link(
            rs.getLong(1),
            rs.getString(2),
            null,
            new Timestamp(rs.getTimestamp(3).getTime()).toLocalDateTime().atOffset(ZoneOffset.UTC),
//            new Date(rs.getDate(3).getTime()).toInstant().atOffset(ZoneOffset.UTC),
            null,
            rs.getLong(4),
            CONVERTOR.convertToEntityAttribute((PGobject) rs.getObject(5))
    );

    private final RowMapper<ChatLink> rowMapperChatLink =
            (rs, rowNum) -> new ChatLink(
                    rs.getLong(1),
                    rs.getLong(2)
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
//                () -> assertTrue(result.getChats().contains(chatData.getId())),
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
        Link linkData = linkRepository.saveAndFlush(
                new Link(
                        null,
                        LINK,
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        11L,
                        new HashMap<>()
                )
        );

        // when
        linkService.updateDataChanges(
                Map.of("commits", "12", "comments", "13"),
                OffsetDateTime.now(),
                linkData.getId()
        );
        System.out.println(OffsetDateTime.now());
        System.out.println(OffsetDateTime.now().toLocalDate());
        System.out.println(LocalDate.now());
        linkRepository.flush();

        // then
        Link result = jdbcTemplate.queryForObject(SELECT_LINK_ID_QUERY, rowMapperLink, linkData.getId());
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getDataChanges().get("commits"), "12"),
                () -> assertEquals(result.getDataChanges().get("comments"), "13"),
                () -> assertEquals(result.getPageUpdatedDate().toLocalDate(), LocalDate.now())
        );
    }
}

//    @Override
//    @Transactional
//    public void updateDataChanges(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId) {
//        Link link = linkRepository.findById(linkId).orElseThrow(
//                () -> new EmptyResultException(String.format("Ссылки с таким (link_id)=(%s) не существует", linkId)));
//        link.setDataChanges(dataChanges);
//        link.setPageUpdatedDate(updatedDate);
//        linkRepository.save(link);
//    }