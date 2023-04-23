package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Links.LINKS;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dsl;
    private static final ConverterJson CONVERTER = new ConverterJson();

    @Override
    public void add(LinkData linkData) {
        checkEntity(linkData);
        dsl.insertInto(LINKS, LINKS.LINK, LINKS.DOMAIN_ID, LINKS.PAGE_UPDATED_DATE, LINKS.DATA_CHANGES)
                .values(linkData.getLink(),
                        linkData.getDomainId(),
                        linkData.getPageUpdateDate().toLocalDateTime(),
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
    public LinkData getByLink(String link) {
        return dsl.select(LINKS.fields())
                .from(LINKS)
                .where(LINKS.LINK.eq(link))
                .fetchOptional()
                .orElseThrow(() -> new EmptyResultDataAccessException(1))
                .map(record -> new RecordLinkMapper().map(record));
    }

    @Override
    public List<LinkData> findAll(String nameField, boolean orderASC, Integer limit) {
        SelectQuery<Record> query = dsl.selectQuery();
        query.addSelect(LINKS.fields());
        query.addFrom(LINKS);

        if (nameField == null || nameField.isEmpty())
            nameField = "id";

        if (orderASC)
            query.addOrderBy(LINKS.field(nameField).asc());
        else
            query.addOrderBy(LINKS.field(nameField).desc());

        if (limit != null)
            query.addLimit(limit);
        return query.fetch().map(record -> new RecordLinkMapper().map(record));
    }

    @Override
    public List<LinkData> getByLinkIds(List<Long> arrChatLink) {
        return dsl.select(LINKS.fields())
                .from(LINKS)
                .where(LINKS.ID.in(arrChatLink))
                .fetch()
                .map(record -> new RecordLinkMapper().map(record));
    }
}
