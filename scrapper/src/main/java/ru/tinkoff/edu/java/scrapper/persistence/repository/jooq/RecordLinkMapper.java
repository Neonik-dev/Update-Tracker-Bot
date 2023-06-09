package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.time.ZoneOffset;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Links.LINKS;

public class RecordLinkMapper {
    private static final ConverterJson CONVERTER = new ConverterJson();

    public @Nullable Link map(Record row) {
        return new Link(
                row.getValue(LINKS.ID),
                row.getValue(LINKS.LINK),
                row.getValue(LINKS.SCHEDULER_UPDATED_DATE).atOffset(ZoneOffset.UTC),
                row.getValue(LINKS.USER_CHECK_DATE).atOffset(ZoneOffset.UTC),
                row.getValue(LINKS.PAGE_UPDATED_DATE).atOffset(ZoneOffset.UTC),
                row.getValue(LINKS.DOMAIN_ID),
                CONVERTER.from(row.getValue(LINKS.DATA_CHANGES).data())
        );
    }
}
