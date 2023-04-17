package ru.tinkoff.edu.java.database.jdbc;

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
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.impl.LinkRepositoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {LinkRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcLinkIT extends IntegrationEnvironment {
    private final JdbcTemplate jdbcTemplate;
    private final LinkRepositoryImpl linkRepository;
    private final JdbcUtils utils;
    private final RowMapper<LinkData> rowMapper = (rs, rowNum) -> {
        LinkData linkDataBD = new LinkData();
        linkDataBD.setLink(rs.getString(1));
        linkDataBD.setDomainId(rs.getLong(2));
        linkDataBD.setDataChanges(new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(3)));
        return linkDataBD;
    };
    private LinkData linkData;

    @BeforeEach
    public void createLinkData() {
        linkData = utils.createLinkData();
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addLinkWithExistsDomainId_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomainId(), utils.DOMAIN_NAME);

        // when
        linkRepository.add(linkData);

        // then
        LinkData result = jdbcTemplate.queryForObject(
                "SELECT link, domain_id, data_changes FROM links where link = ?",
                rowMapper,
                linkData.getLink()
        );
        assertResult(result);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkWithNoExistsDomainId_ThrowsForeignKeyNotExistsException() {
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
    @SneakyThrows
    @Transactional
    @Rollback
    void getByExistsName_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomainId(), utils.DOMAIN_NAME);
        linkRepository.add(linkData);

        // when
        LinkData result = linkRepository.getByLink(linkData.getLink());

        // then
        assertResult(result);
    }

    @Test
    @Transactional
    @Rollback
    void getByNotExistsName_OK() {
        // given

        // when/then
        assertThrows(EmptyResultException.class, () -> linkRepository.getByLink(linkData.getLink()));
    }

    void assertResult(LinkData result) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getLink(), linkData.getLink()),
                () -> assertEquals(result.getDomainId(), linkData.getDomainId()),
                () -> assertEquals(result.getDataChanges().get("commits"), "15"),
                () -> assertEquals(result.getDataChanges().get("comments"), "11")
        );
    }

    @SneakyThrows
    private void addDomainAndLink() {
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", linkData.getDomainId(), utils.DOMAIN_NAME);
        linkRepository.add(linkData);
    }
}
