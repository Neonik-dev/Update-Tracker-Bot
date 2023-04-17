package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.impl.DomainRepositoryImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {DomainRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcDomainIT extends IntegrationEnvironment {
    private final DomainRepositoryImpl domainRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);
    private final JdbcUtils utils;
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
        assertResult(result);
    }

    @Test
    @Transactional
    @Rollback
    void addExistsDomainName_ThrowsDuplicateUniqueFieldException() {
        // given
        jdbcTemplate.update("INSERT INTO domains(name) VALUES (?)", domainData.getName());

        // when/then
        assertThrows(DuplicateUniqueFieldException.class, () -> domainRepository.add(domainData));
    }

    @Test
    @Transactional
    @Rollback
    void addExistsDomainId_ThrowsDuplicateUniqueFieldException() {
        // given
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // when/then
        assertThrows(DuplicateUniqueFieldException.class, () -> domainRepository.add(domainData));
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
        assertResult(result);
    }

    @Test
    @Transactional
    @Rollback
    void getByNotExistsName_OK() {
        // given

        // when/then
        assertThrows(EmptyResultException.class, () -> domainRepository.getByName(domainData.getName()));
    }

    void assertResult(DomainData result) {
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getName(), domainData.getName()),
                () -> assertEquals(result.getCreatedDate(), LocalDate.now())
        );
    }
}
