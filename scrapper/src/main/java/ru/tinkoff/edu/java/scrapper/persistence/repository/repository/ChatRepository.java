package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;

import java.util.List;

@Repository
public interface ChatRepository {
    void add(ChatData chatData) throws DuplicateUniqueFieldException, BadEntityException;
    void remove(long id);
    List<ChatData> findAll();
}
