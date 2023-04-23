package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.LinkData;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface LinkRepository {
    default void checkEntity(LinkData linkData) throws BadEntityException {
        if (linkData == null || linkData.getLink() == null
                || linkData.getDataChanges() == null || linkData.getDomainId() == null)
            throw new BadEntityException();
    }
    void add(LinkData linkData);
    void remove(long id);
    void remove(String link);
    void updateUpdatedDateLink(long linkId);

    void updateDataChangesLink(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId);

    LinkData getByLink(String link);
    List<LinkData> findAll(String nameField, boolean orderASC, Integer limit);
    List<LinkData> getByLinkIds(List<Long> arrChatLink);
}
