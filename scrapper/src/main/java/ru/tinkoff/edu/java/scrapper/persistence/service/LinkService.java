package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface LinkService {
    LinkData add(long chatId, URI url) throws EmptyResultException, ForeignKeyNotExistsException, BadEntityException, DuplicateUniqueFieldException;
    LinkData remove(long chatId, URI url) throws EmptyResultException;
    Collection<LinkData> listAll(long tgChatId);

    void updateDataChanges(Map<String, String> dataChanges, Long linkId);

    Optional<LinkData> getOldestUpdateLink();
}
