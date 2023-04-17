package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
    private final JdbcTemplate template;
    private final RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);

    private static final String INSERT_QUERY = "INSERT INTO chats(id) VALUES (?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM chats";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM chats WHERE id=?";

    void checkEntity(ChatData chatData) throws BadEntityException {
        if (chatData == null || chatData.getId() == null)
            throw new BadEntityException();
    }

    @Override
    public void add(ChatData chatData) throws BadEntityException {
        checkEntity(chatData);
        template.update(INSERT_QUERY, chatData.getId());
    }

    @Override
    public List<ChatData> findAll() {
        return template.query(SELECT_ALL_QUERY, rowMapper);
    }

    @Override
    public void remove(long id) {
        template.update(DELETE_BY_ID_QUERY, id);
    }
}
