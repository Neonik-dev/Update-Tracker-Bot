package ru.tinkoff.edu.java.scrapper.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class LinkData {
    @Id
    private long id;
    private String link;
    private OffsetDateTime schedulerUpdateDate;
    private OffsetDateTime pageUpdateDate;
    private OffsetDateTime userCheckDate;
    private Long domainId;
    private Map<String, String> dataChanges;
}
