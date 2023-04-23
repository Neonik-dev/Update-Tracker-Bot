package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.Links;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

public interface LinkService {
    LinkResponse add(long chatId, URI url);
    LinkResponse remove(long chatId, URI url);
    ListLinksResponse listAll(long tgChatId);

    void updateDataChanges(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId);

//    Optional<LinkData> getOldestUpdateLink();
    Optional<Links> getOldestUpdateLink();
}
