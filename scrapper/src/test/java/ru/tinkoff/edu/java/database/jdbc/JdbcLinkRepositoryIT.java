package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JdbcLinkRepository.class, DBConfiguration.class, TestConfiguration.class, Utils.class})
public class JdbcLinkRepositoryIT extends IntegrationEnvironment {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkRepository linkRepository;
    private final Utils utils;
    private final RowMapper<Link> rowMapper = (rs, rowNum) -> new Link(
            null,
            rs.getString(1),
            null, null, null,
            rs.getLong(2),
            new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(3))
    );
    private Link linkData;

    @BeforeEach
    public void createLinkData() {
        linkData = utils.createLinkData();
    }

    @Test
    @Transactional
    @Rollback
    void addLinkWithExistsDomainId_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomain().getId(), utils.DOMAIN_NAME);

        // when
        linkRepository.add(linkData);

        // then
        Link result = jdbcTemplate.queryForObject(
                "SELECT link, domain_id, data_changes FROM links where link = ?",
                rowMapper,
                linkData.getLink()
        );
        utils.assertLinkResult(result, linkData);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkWithNoExistsDomainId_ThrowsDataIntegrityViolationException() {
        // given

        // then/when
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(linkData));
    }

    @Test
    @Transactional
    @Rollback
    void addExistsLink_ThrowsDataIntegrityViolationException() {
        // given
        addDomainAndLink();

        // then/when
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(linkData));
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
        // given/then/when
        assertThrows(BadEntityException.class, () -> linkRepository.add(new Link()));
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
        assertTrue(utils.checkMissingDataLink(linkData.getLink()));
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
        assertTrue(utils.checkMissingDataLink(linkData.getLink()));
    }

    @Test
    @Transactional
    @Rollback
    void getByExistsName_OK() {
        // given
        addDomainAndLink();

        // when
        Link result = linkRepository.getByLink(linkData.getLink());

        // then
        assertResult(result);
    }

    @Test
    @Transactional
    @Rollback
    void getByNotExistsName_ThrowsEmptyResultDataAccessException() {
        // given

        // when/then
        assertThrows(EmptyResultDataAccessException.class, () -> linkRepository.getByLink(linkData.getLink()));
    }

    void assertResult(Link result) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getLink(), linkData.getLink()),
                () -> assertEquals(result.getDomain().getId(), linkData.getDomain().getId()),
                () -> assertEquals(result.getDataChanges().get("commits"), "15"),
                () -> assertEquals(result.getDataChanges().get("comments"), "11")
        );
    }

    private void addDomainAndLink() {
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomain().getId(), utils.DOMAIN_NAME);
        linkRepository.add(linkData);
    }
}
