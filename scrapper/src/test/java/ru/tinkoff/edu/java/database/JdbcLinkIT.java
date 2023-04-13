package ru.tinkoff.edu.java.database;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepositoryImpl;

import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {LinkRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class})
public class JdbcLinkIT {
    private final JdbcTemplate jdbcTemplate;
    private final LinkRepositoryImpl linkRepository;
    private final RowMapper<LinkData> rowMapper = (rs, rowNum) -> {
        LinkData linkDataBD = new LinkData();
        linkDataBD.setLink(rs.getString(1));
        linkDataBD.setDomainId(rs.getLong(2));
        linkDataBD.setDataChanges(new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(3)));
        return linkDataBD;
    };
    private final String LINK = "http://noNameSiteForTest.com/no-name-site-for-test";
    private final String DOMAIN = "noNameSiteForTest";
    private LinkData linkData;

    @BeforeEach
    public void createLinkData() {
        long domainId;
        do {
            domainId = new Random().nextLong();
        } while (!checkMissingDomainId(domainId));

        linkData = new LinkData();
        linkData.setLink(LINK);
        linkData.setDataChanges(Map.of("commits", "15", "comments", "11"));
        linkData.setDomainId(domainId);
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addLinkWithExistsDomainId_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomainId(), DOMAIN);

        // then
        linkRepository.add(linkData);

        // when
        LinkData result = jdbcTemplate.queryForObject(
                "SELECT link, domain_id, data_changes FROM links where link = ?",
                rowMapper,
                linkData.getLink()
        );
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getLink(), linkData.getLink()),
                () -> assertEquals(result.getDomainId(), linkData.getDomainId()),
                () -> assertEquals(result.getDataChanges().get("commits"), "15"),
                () -> assertEquals(result.getDataChanges().get("comments"), "11")
        );
    }

    @Test
    @Transactional
    @Rollback
    void addLinkWithNoExistsDomainId_Throws() {
        // given

        // then/when
        assertThrows(ForeignKeyNotExistsException.class, () -> linkRepository.add(linkData));
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addExistsLink_ThrowsDuplicateUniqueFieldException() {
        // given
        addDomainAndLink();

        // then/when
        assertThrows(DuplicateUniqueFieldException.class, () -> linkRepository.add(linkData));
    }

    @Test
    @Transactional
    @Rollback
    void addNullLinkData_ThrowsBadEntityException() {
        // given
        linkData = null;

        // then/when
        assertThrows(BadEntityException.class, () -> linkRepository.add(linkData));
    }

    @Test
    @Transactional
    @Rollback
    void addEmptyLinkData_ThrowsBadEntityException() {
        // given
        linkData = new LinkData();

        // then/when
        assertThrows(BadEntityException.class, () -> linkRepository.add(linkData));
    }

    @Test
    @Transactional
    @Rollback
    void removeById_OK() {
        // given
        addDomainAndLink();
        Long linkId = jdbcTemplate.queryForObject("SELECT id FROM links where link = ?", Long.class, linkData.getLink());

        // when
        linkRepository.remove(linkId);

        // then
        assertTrue(checkMissingLink(linkData.getLink()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByLink_OK() {
        // given
        addDomainAndLink();

        // when
        linkRepository.remove(linkData.getLink());

        // then
        assertTrue(checkMissingLink(linkData.getLink()));
    }

    private boolean checkMissingLink(String link) {
        Integer result = jdbcTemplate.queryForObject("SELECT count(*) FROM links where link = ?", Integer.class, link);
        return result == null || result == 0;
    }

    private boolean checkMissingDomainId(Long domainId) {
        Integer result = jdbcTemplate.queryForObject("SELECT count(*) FROM domains where id = ?", Integer.class, domainId);
        return result == null || result == 0;
    }

    @SneakyThrows
    private void addDomainAndLink() {
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomainId(), DOMAIN);
        linkRepository.add(linkData);
    }

}
