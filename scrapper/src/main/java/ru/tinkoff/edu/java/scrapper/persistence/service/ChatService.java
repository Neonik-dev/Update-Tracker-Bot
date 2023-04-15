package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;

public interface ChatService {
        void register(Long tgChatId) throws DuplicateUniqueFieldException, BadEntityException;
        void unregister(Long tgChatId);
}
