package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;

import java.util.List;

public interface ChatRepository {
    default void checkEntity(Chat chatData) throws BadEntityException {
        if (chatData == null || chatData.getId() == null) {
            throw new BadEntityException();
        }
    }

    default void checkChatId(Long chatId) throws BadEntityException {
        if (chatId == null) {
            throw new BadEntityException();
        }
    }

    void add(Chat chatData);

    void remove(Long id);

    List<Chat> findAll();
}
