package ru.tinkoff.edu.java.database;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.controllers.TgChatController;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepositoryImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = { ChatRepositoryImpl.class, DBConfiguration.class })
//@ComponentScan(basePackages = {"ru.tinkoff.edu.java.scrapper.persistence.repository","ru.tinkoff.edu.java.scrapper.configuration"})
//@ContextConfiguration({"classpath*:spring/applicationContext.xml"})
public class JdbcChatIT extends IntegrationEnvironment{
    //    @Autowired
    private final ChatRepositoryImpl chatRepository;
//    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);

    @Test
    @Transactional
    @Rollback
    void addChat_OK() {
        // given
        long chat_id;
        Integer countRow;
        do {
            chat_id = new Random().nextLong();
            countRow = jdbcTemplate.queryForObject("SELECT count(*) FROM chats where id = ?", Integer.class, chat_id);
            System.out.println(countRow);
        } while (countRow != null && countRow != 0);

        ChatData chatData = new ChatData();
        chatData.setId(chat_id);

        // when
        chatRepository.add(chatData);

        // then
        ChatData result = jdbcTemplate.queryForObject("SELECT * FROM chats where id = ?", rowMapper, chat_id);
        System.out.println(result.getLastCallDate());
        assert result != null;
        assertEquals(result.getId(), chat_id);
        assertEquals(result.getLastCallDate(), LocalDate.now());
    }

    @Transactional
    @Rollback
    void removeTest() {
    }
}
