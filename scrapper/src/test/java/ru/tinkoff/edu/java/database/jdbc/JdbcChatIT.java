package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.scrapper.persistence.repository.impl.ChatRepositoryImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {ChatRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcChatIT extends IntegrationEnvironment {
    private final ChatRepositoryImpl chatRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);
    private final JdbcUtils utils;
    private ChatData chatData;

    @BeforeEach
    public void initChatData() {
        chatData = utils.createChatData();
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addUniqueChat_OK() {
        // given

        // when
        chatRepository.add(chatData);

        // then
        assertResult(chatData.getId());
    }

    @Test
    void addExistsChat_ThrowsDuplicateUniqueFieldException() {
        // given
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());

        // then/when
        assertThrows(DuplicateUniqueFieldException.class, () -> chatRepository.add(chatData));

        // then
        assertResult(chatData.getId());
        jdbcTemplate.update("DELETE FROM chats WHERE id=?", chatData.getId());
    }

    @Test
    void addNullChatId_ThrowsBadEntityException() {
        // given
        chatData.setId(null);

        // then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(chatData));
    }

    @Test
    void addNullChat_ThrowsBadEntityException() {
        // given
        ChatData nullChatData = null;

        // then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(nullChatData));
    }

    @Test
    @Transactional
    @Rollback
    void removeExistsChatId_OK () {
        // given
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistsChatId_OK () {
        // given

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    private void assertResult(long chatId) {
        ChatData result = jdbcTemplate.queryForObject("SELECT * FROM chats where id = ?", rowMapper, chatId);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getId(), chatId),
                () -> assertEquals(result.getLastCallDate(), LocalDate.now())
        );
    }
}
