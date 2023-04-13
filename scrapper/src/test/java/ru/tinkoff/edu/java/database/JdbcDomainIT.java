package ru.tinkoff.edu.java.database;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.DomainRepositoryImpl;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {DomainRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class})
public class JdbcDomainIT {
    private final DomainRepositoryImpl domainRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);
    private final String NAME = "noNameSiteForTest";

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addUniqueDomainName_OK() {
        // given
        DomainData domainData = new DomainData();
        domainData.setName(NAME);

        // when
        domainRepository.add(domainData);

        // then
        DomainData result = jdbcTemplate.queryForObject("SELECT * FROM domains where name = ?", rowMapper, domainData.getName());
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getName(), domainData.getName()),
                () -> assertEquals(result.getCreatedDate(), LocalDate.now())
        );
    }

    @Test
    @Transactional
    @Rollback
    void addExistsDomainName_ThrowsDuplicateUniqueFieldException() {
        // given
        DomainData domainData = new DomainData();
        domainData.setName(NAME);
        jdbcTemplate.update("INSERT INTO domains(name) VALUES (?)", domainData.getName());

        // when/then
        assertThrows(DuplicateUniqueFieldException.class, () -> domainRepository.add(domainData));
    }

    @Test
    @Transactional
    @Rollback
    void addExistsDomainId_ThrowsDuplicateUniqueFieldException() {
        // given
        DomainData domainData = createDomainData();
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // when/then
        assertThrows(DuplicateUniqueFieldException.class, () -> domainRepository.add(domainData));
    }

    @Test
    @Transactional
    @Rollback
    void removeByExistsId_OK() {
        // given
        DomainData domainData = createDomainData();
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // then
        domainRepository.remove(domainData.getId());

        // when
        assertTrue(checkMissingData(domainData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByNotExistsId_OK() {
        // given
        DomainData domainData = createDomainData();

        // then
        domainRepository.remove(domainData.getId());

        // when
        assertTrue(checkMissingData(domainData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByExistsName_OK() {
        // given
        DomainData domainData = createDomainData();
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());

        // then
        domainRepository.remove(domainData.getName());

        // when
        assertTrue(checkMissingData(domainData.getName()));
    }

    @Test
    @Transactional
    @Rollback
    void removeByNotExistsName_OK() {
        // given
        DomainData domainData = createDomainData();

        // then
        domainRepository.remove(domainData.getName());

        // when
        assertTrue(checkMissingData(domainData.getName()));
    }

    private DomainData createDomainData() {
        long domainId;
        do {
            domainId = new Random().nextLong();
        } while (!checkMissingData(domainId));

        DomainData domainData = new DomainData();
        domainData.setName(NAME);
        domainData.setId(domainId);
        return domainData;
    }

    private boolean checkMissingData(Long domainId) {
        Integer result = jdbcTemplate.queryForObject("SELECT count(*) FROM domains where id = ?", Integer.class, domainId);
        return result == null || result == 0;
    }
    private boolean checkMissingData(String name) {
        Integer result = jdbcTemplate.queryForObject("SELECT count(*) FROM domains where name = ?", Integer.class, name);
        return result == null || result == 0;
    }


}
