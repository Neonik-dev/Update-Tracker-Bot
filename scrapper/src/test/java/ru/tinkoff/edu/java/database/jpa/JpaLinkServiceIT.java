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
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;

import java.net.URI;
import java.time.ZoneOffset;
import java.util.Date;

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
    private static final ConvertorFromMapToJson CONVERTOR = new ConvertorFromMapToJson();
    private final RowMapper<Link> rowMapper = (rs, rowNum) -> new Link(
            rs.getLong(1),
            rs.getString(2),
            null,
            new Date(rs.getDate(3).getTime()).toInstant().atOffset(ZoneOffset.UTC),
            null,
            rs.getLong(4),
            CONVERTOR.convertToEntityAttribute((PGobject) rs.getObject(5))
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
        Link result = jdbcTemplate.queryForObject(SELECT_BY_LINK_QUERY, rowMapper, LINK);
        assertAll(
                () -> assertEquals(result.getLink(), LINK),
//                () -> assertTrue(result.getChats().contains(chatData.getId())),
                () -> assertEquals(
                        result.getDomain().getId(),
                        domainRepository.findByName(URI.create(LINK).getHost()).getId()
                )
        );
    }
}
