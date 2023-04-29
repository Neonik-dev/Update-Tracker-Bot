package ru.tinkoff.edu.java.database.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.ConverterJson;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Domains.DOMAINS;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Links.LINKS;

@JooqTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JooqDomainRepository.class, JooqLinkRepository.class, DBConfiguration.class, Utils.class})
public class JooqLinkRepositoryIT extends IntegrationEnvironment {
    private final DSLContext dsl;
    private final JooqLinkRepository linkRepository;
    private final Utils utils;
    private Link linkData;
    private final ConverterJson converter = new ConverterJson();

    @BeforeEach
    public void createLinkData() {
        linkData = utils.createLinkData();
    }

    @Test
    void addLinkWithExistsDomainId_OK() {
        // given
        dsl.insertInto(DOMAINS, DOMAINS.ID, DOMAINS.NAME)
                .values(linkData.getDomain().getId(), utils.DOMAIN_NAME)
                .execute();

        // when
        linkRepository.add(linkData);

        // then
        Link result = dsl.select(LINKS.LINK, LINKS.DOMAIN_ID, LINKS.DATA_CHANGES)
                .from(LINKS)
                .where(LINKS.LINK.eq(linkData.getLink()))
                .fetchOne()
                .map(record -> new Link(
                            null,
                                record.getValue(LINKS.LINK),
                                null, null, null,
                                record.getValue(LINKS.DOMAIN_ID),
                                converter.from(record.getValue(LINKS.DATA_CHANGES).data())
                        )
                );

        utils.assertLinkResult(result, linkData);
    }

    @Test
    void addLinkWithNoExistsDomainId_ThrowsDataIntegrityViolationException() {
        // given

        // then/when
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(linkData));
    }

    @Test
    void addExistsLink_ThrowsDataIntegrityViolationException() {
        // given
//        addDomainAndLink();

        // then/when
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(linkData));
    }

    @Test
    void addNullLinkData_ThrowsBadEntityException() {
        // given
        linkData = null;

        // then/when
        assertThrows(BadEntityException.class, () -> linkRepository.add(linkData));
    }

    @Test
    void addEmptyLinkData_ThrowsBadEntityException() {
        // given/then/when
        assertThrows(BadEntityException.class, () -> linkRepository.add(new Link()));
    }

    @Test
    void removeById_OK() {
        // given
        addDomainAndLink();
        Long linkId = dsl.select(LINKS.ID)
                .from(LINKS)
                .where(LINKS.LINK.eq(linkData.getLink()))
                .fetchOneInto(Long.class);

        // when
        linkRepository.remove(linkId);

        // then
        assertTrue(utils.checkMissingDataLink(linkData.getLink()));
    }

    @Test
    void removeByLink_OK() {
        // given
        addDomainAndLink();

        // when
        linkRepository.remove(linkData.getLink());

        // then
        assertTrue(utils.checkMissingDataLink(linkData.getLink()));
    }

    @Test
    void getByExistsName_OK() {
        // given
        addDomainAndLink();

        // when
        Link result = linkRepository.getByLink(linkData.getLink());

        // then
        utils.assertLinkResult(result, linkData);
    }

    @Test
    void getByNotExistsName_ThrowsEmptyResultDataAccessException() {
        // given

        // when/then
        assertThrows(EmptyResultDataAccessException.class, () -> linkRepository.getByLink(linkData.getLink()));
    }

    private void addDomainAndLink() {
        dsl.insertInto(DOMAINS, DOMAINS.ID, DOMAINS.NAME).values(linkData.getDomain().getId(), utils.DOMAIN_NAME).execute();
        linkRepository.add(linkData);
    }
}
