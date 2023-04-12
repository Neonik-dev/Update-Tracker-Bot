package ru.tinkoff.edu.java.scrapper.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
    private final JdbcTemplate template;
    private final RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);

    @Override
    public void add(ChatData chat) {
        template.update("INSERT INTO chats(id) VALUES (?)", chat.getId());
        printAll();
    }

    @Override
    public List<ChatData> findAll() {
        return template.query("SELECT * FROM chats", rowMapper);
    }

    private void printAll() {
        findAll().forEach(System.out::println);
    }


    @Override
    public void remove(long id) {
        template.update("DELETE FROM chats WHERE id=?", id);
        printAll();
    }
}
