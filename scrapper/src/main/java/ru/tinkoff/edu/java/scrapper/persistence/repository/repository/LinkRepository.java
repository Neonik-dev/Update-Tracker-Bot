package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface LinkRepository {
    default void checkEntity(Link linkData) throws BadEntityException {
        if (linkData == null || linkData.getLink() == null
                || linkData.getDataChanges() == null || linkData.getDomain().getId() == null)
            throw new BadEntityException();
    }
    void add(Link linkData);
    void remove(long id);
    void remove(String link);
    void updateUpdatedDateLink(long linkId);

    void updateDataChangesLink(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId);

    Link getByLink(String link);
    List<Link> findAll(String nameField, boolean orderASC, Integer limit);
    List<Link> getByLinkIds(List<Long> arrChatLink);
}
