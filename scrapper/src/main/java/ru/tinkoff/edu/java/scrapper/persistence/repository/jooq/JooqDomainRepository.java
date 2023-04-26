package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.EmptyResultDataAccessException;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Domains.DOMAINS;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Domain;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;

import java.util.List;

@RequiredArgsConstructor
public class JooqDomainRepository implements DomainRepository {
    private final DSLContext dsl;

    @Override
    public void add(Domain domainData) {
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
    public Domain getByName(String name) {
        return dsl.select(DOMAINS.fields())
                .from(DOMAINS)
                .where(DOMAINS.NAME.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new EmptyResultDataAccessException(1))
                .into(Domain.class);
    }

    @Override
    public List<Domain> findAll() {
        return dsl.select(DOMAINS.fields())
                .from(DOMAINS)
                .fetch().into(Domain.class);
    }
}
