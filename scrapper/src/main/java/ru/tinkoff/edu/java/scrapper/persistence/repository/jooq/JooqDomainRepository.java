package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Domains.DOMAINS;

import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class JooqDomainRepository implements DomainRepository {
    private final DSLContext dsl;

    @Override
    public void add(DomainData domainData) {
        checkEntity(domainData);
        dsl.insertInto(DOMAINS, DOMAINS.NAME).values(domainData.getName()).execute();
    }

    @Override
    public void remove(long id) {
        dsl.delete(DOMAINS).where(DOMAINS.ID.eq(id)).execute();
    }

    @Override
    public void remove(String name) {
        dsl.delete(DOMAINS).where(DOMAINS.NAME.eq(name)).execute();
    }

    @Override
    public DomainData getByName(String name) {
        return dsl.select(DOMAINS.fields())
                .from(DOMAINS)
                .where(DOMAINS.NAME.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new EmptyResultDataAccessException(1))
                .into(DomainData.class);
    }

    @Override
    public List<DomainData> findAll() {
        return dsl.select(DOMAINS.fields())
                .from(DOMAINS)
                .fetch().into(DomainData.class);
    }
}
