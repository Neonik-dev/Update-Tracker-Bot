package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;

import java.util.List;

@Repository
public interface ChatLinkRepository {
    void add(ChatLinkData chatLinkData) throws DuplicateUniqueFieldException, BadEntityException, ForeignKeyNotExistsException;
    void remove(long chatId, long linkId);
    List<ChatLinkData> findAll();
}
