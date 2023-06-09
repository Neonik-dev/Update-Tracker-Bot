package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Links.LINKS;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private static final ConverterJson CONVERTER = new ConverterJson();
    private final DSLContext dsl;

    @Override
    public void add(Link linkData) {
        checkEntity(linkData);
        dsl.insertInto(LINKS, LINKS.LINK, LINKS.DOMAIN_ID, LINKS.PAGE_UPDATED_DATE, LINKS.DATA_CHANGES)
                .values(linkData.getLink(),
                        linkData.getDomain().getId(),
                        linkData.getPageUpdatedDate().toLocalDateTime(),
                        JSONB.jsonb(CONVERTER.to(linkData.getDataChanges()))
                ).execute();
    }

    @Override
    public void remove(long id) {
        dsl.delete(LINKS)
                .where(LINKS.ID.eq(id))
                .execute();
    }

    @Override
    public void remove(String link) {
        dsl.delete(LINKS)
                .where(LINKS.LINK.eq(link))
                .execute();
    }

    @Override
    public void updateUpdatedDateLink(long linkId) {
        dsl.update(LINKS)
                .set(LINKS.SCHEDULER_UPDATED_DATE, OffsetDateTime.now().toLocalDateTime())
                .where(LINKS.ID.eq(linkId))
                .execute();
    }

    @Override
    public void updateDataChangesLink(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId) {
        dsl.update(LINKS)
                .set(LINKS.DATA_CHANGES, JSONB.jsonb(CONVERTER.to(dataChanges)))
                .set(LINKS.PAGE_UPDATED_DATE, updatedDate.toLocalDateTime())
                .where(LINKS.ID.eq(linkId))
                .execute();
    }

    @Override
    public Link getByLink(String link) {
        return dsl.select(LINKS.fields())
                .from(LINKS)
                .where(LINKS.LINK.eq(link))
                .fetchOptional()
                .orElseThrow(() -> new EmptyResultDataAccessException(1))
                .map(row -> new RecordLinkMapper().map(row));
    }

    @Override
    public List<Link> findAll(String nameField, boolean orderASC, Integer limit) {
        SelectQuery<Record> query = dsl.selectQuery();
        query.addSelect(LINKS.fields());
        query.addFrom(LINKS);

        String name = nameField == null || nameField.isEmpty() ? "id" : nameField;

        if (orderASC) {
            query.addOrderBy(LINKS.field(name).asc());
        } else {
            query.addOrderBy(LINKS.field(name).desc());
        }

        if (limit != null) {
            query.addLimit(limit);
        }
        return query.fetch().map(row -> new RecordLinkMapper().map(row));
    }

    @Override
    public List<Link> getByLinkIds(List<Long> arrChatLink) {
        return dsl.select(LINKS.fields())
                .from(LINKS)
                .where(LINKS.ID.in(arrChatLink))
                .fetch()
                .map(row -> new RecordLinkMapper().map(row));
    }
}
