package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;

import java.util.List;

@Repository
public interface ChatRepository {
    void add(ChatData chat);
    void remove(long id);
    List<ChatData> findAll();
}
