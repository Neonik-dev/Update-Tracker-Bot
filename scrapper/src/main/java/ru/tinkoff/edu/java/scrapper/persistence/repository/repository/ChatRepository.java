package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;

import java.util.List;

public interface ChatRepository {
    void add(ChatData chatData) throws DuplicateUniqueFieldException, BadEntityException;
    void remove(long id);
    List<ChatData> findAll();
}
