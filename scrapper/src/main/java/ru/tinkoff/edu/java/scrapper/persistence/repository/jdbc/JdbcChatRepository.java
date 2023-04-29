package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;

import java.util.List;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate template;
    private final RowMapper<Chat> rowMapper = new DataClassRowMapper<>(Chat.class);

    private static final String INSERT_QUERY = "INSERT INTO chats(id, created_date, last_call_date) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM chats";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM chats WHERE id=?";

    @Override
    public void add(Chat chatData) throws BadEntityException {
        checkEntity(chatData);
        template.update(INSERT_QUERY, chatData.getId(), chatData.getCreatedDate(), chatData.getLastCallDate());
    }

    @Override
    public List<Chat> findAll() {
        return template.query(SELECT_ALL_QUERY, rowMapper);
    }

    @Override
    public void remove(Long id) {
        checkChatId(id);
        template.update(DELETE_BY_ID_QUERY, id);
    }
}
