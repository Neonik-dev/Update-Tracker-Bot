package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatData;

import java.util.List;

public interface ChatRepository {
    default void checkEntity(ChatData chatData) throws BadEntityException {
        if (chatData == null || chatData.getId() == null)
            throw new BadEntityException();
    }
    void add(ChatData chatData);
    void remove(long id);
    List<ChatData> findAll();
}
