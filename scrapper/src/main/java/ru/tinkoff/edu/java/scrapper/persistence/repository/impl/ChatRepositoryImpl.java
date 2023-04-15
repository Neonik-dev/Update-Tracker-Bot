package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
    private final JdbcTemplate template;
    private final RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);

    void checkEntity(ChatData chatData) throws BadEntityException {
        if (chatData == null || chatData.getId() == null)
            throw new BadEntityException();
    }

    @Override
    public void add(ChatData chatData) throws DuplicateUniqueFieldException, BadEntityException {
        checkEntity(chatData);
        try {
            template.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("Пользователь с таким id уже существует");
        }
    }

    @Override
    public List<ChatData> findAll() {
        return template.query("SELECT * FROM chats", rowMapper);
    }

    @Override
    public void remove(long id) {
        template.update("DELETE FROM chats WHERE id=?", id);
    }
}
