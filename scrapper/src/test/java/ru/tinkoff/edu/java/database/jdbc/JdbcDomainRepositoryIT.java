package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.JdbcUtils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcDomainRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JdbcDomainRepository.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcDomainRepositoryIT extends IntegrationEnvironment {
    private final JdbcDomainRepository domainRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUtils utils;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);
    private DomainData domainData;

    @BeforeEach
    public void initDomainData() {
        domainData = utils.createDomainData();
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addUniqueDomainName_OK() {
        // given

        // when
        domainRepository.add(domainData);

        // then
        DomainData result = jdbcTemplate.queryForObject("SELECT * FROM domains where name = ?", rowMapper, domainData.getName());
        utils.assertDomainResult(result, domainData);
    }

    @Test
    @Transactional
    @Rollback
    void addExistsDomainName_ThrowsDuplicateKeyException() {
        // given
        jdbcTemplate.update("INSERT INTO domains(name) VALUES (?)", domainData.getName());

        // when/then
        assertThrows(DuplicateKeyException.class, () -> domainRepository.add(domainData));
    }

    @Test
    @Transactional
    @Rollback
    void removeByExistsId_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // when
        domainRepository.remove(domainData.getId());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByNotExistsId_OK() {
        // given

        // when
        domainRepository.remove(domainData.getId());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByExistsName_OK() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // when
        domainRepository.remove(domainData.getName());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getName()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByNotExistsName_OK() {
        // given

        // when
        domainRepository.remove(domainData.getName());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getName()));
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void getByExistsName_OK() {
        // given
        domainRepository.add(domainData);

        // when
        DomainData result = domainRepository.getByName(domainData.getName());

        // then
        utils.assertDomainResult(result, domainData);
    }

    @Test
    @Transactional
    @Rollback
    void getByNotExistsName_ThrowsEmptyResultDataAccessException() {
        // given

        // when/then
        assertThrows(EmptyResultDataAccessException.class, () -> domainRepository.getByName(domainData.getName()));
    }
}
