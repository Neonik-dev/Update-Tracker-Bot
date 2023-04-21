package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;

import java.time.ZoneOffset;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Links.LINKS;

public class RecordLinkMapper{
    private static final ConverterJson CONVERTER = new ConverterJson();

    public @Nullable LinkData map(Record record) {
        return LinkData.builder()
                .id(record.getValue(LINKS.ID))
                .link(record.getValue(LINKS.LINK))
                .domainId(record.getValue(LINKS.DOMAIN_ID))
                .schedulerUpdateDate(record.getValue(LINKS.SCHEDULER_UPDATED_DATE).atOffset(ZoneOffset.UTC))
                .userCheckDate(record.getValue(LINKS.USER_CHECK_DATE).atOffset(ZoneOffset.UTC))
                .pageUpdateDate(record.getValue(LINKS.PAGE_UPDATED_DATE).atOffset(ZoneOffset.UTC))
                .dataChanges(
                        CONVERTER.from(record.getValue(LINKS.DATA_CHANGES).data())
                ).build();
    }
}
