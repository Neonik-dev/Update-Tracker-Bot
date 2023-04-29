package ru.tinkoff.edu.java.database.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Domain;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqDomainRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Domains.DOMAINS;

@JooqTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JooqDomainRepository.class, DBConfiguration.class, Utils.class})
public class JooqDomainRepositoryIT extends IntegrationEnvironment {
    private final DSLContext dsl;
    private final JooqDomainRepository domainRepository;
    private final Utils utils;
    private Domain domainData;

    @BeforeEach
    public void initDomainData() {
        domainData = utils.createDomainData();
    }

    @Test
    public void addUniqueDomainName_OK() {
        // given

        // when
        domainRepository.add(domainData);

        // then
        Domain result = dsl.select(DOMAINS.fields())
                .from(DOMAINS)
                .where(DOMAINS.NAME.eq(domainData.getName()))
                .fetchOneInto(Domain.class);

        utils.assertDomainResult(result, domainData);
    }

    @Test
    public void addExistsDomainName_ThrowsDuplicateKeyException() {
        // given
        domainRepository.add(domainData);
//        jdbcTemplate.update("INSERT INTO domains(name) VALUES (?)", domainData.getName());

        // when/then
        assertThrows(DuplicateKeyException.class, () -> domainRepository.add(domainData));
    }

    @Test
    @Transactional
    @Rollback
    void addNullDomain_ThrowsBadEntityException() {
        // given
        domainData.setName(null);

        // when/then
        assertAll(
                () -> assertThrows(BadEntityException.class, () -> domainRepository.add(null)),
                () -> assertThrows(BadEntityException.class, () -> domainRepository.add(domainData)),
                () -> assertThrows(BadEntityException.class, () -> domainRepository.add(new Domain()))
        );
    }

    @Test
    void removeByExistsId_OK() {
        // given
        dsl.insertInto(DOMAINS, DOMAINS.ID, DOMAINS.NAME).values(domainData.getId(), domainData.getName()).execute();

        // when
        domainRepository.remove(domainData.getId());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getId()));
    }

    @Test
    void removeByNotExistsId_OK() {
        // given

        // when
        domainRepository.remove(domainData.getId());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getId()));
    }

    @Test
    void removeByExistsName_OK() {
        // given
        dsl.insertInto(DOMAINS, DOMAINS.ID, DOMAINS.NAME).values(domainData.getId(), domainData.getName()).execute();

        // when
        domainRepository.remove(domainData.getName());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getName()));
    }

    @Test
    void removeByNotExistsName_OK() {
        // given

        // when
        domainRepository.remove(domainData.getName());

        // then
        assertTrue(utils.checkMissingDataDomain(domainData.getName()));
    }

    @Test
    void getByExistsName_OK() {
        // given
        domainRepository.add(domainData);

        // when
        Domain result = domainRepository.getByName(domainData.getName());

        // then
        utils.assertDomainResult(result, domainData);
    }

    @Test
    void getByNotExistsName_ThrowsEmptyResultDataAccessException() {
        // given

        // when/then
        assertThrows(EmptyResultDataAccessException.class, () -> domainRepository.getByName(domainData.getName()));
    }
}
