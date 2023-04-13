package ru.tinkoff.edu.java.database;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepositoryImpl;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {ChatRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class})
public class JdbcChatIT {
    private final ChatRepositoryImpl chatRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ChatData> rowMapper = new DataClassRowMapper<>(ChatData.class);

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addUniqueChat_OK() {
        // given
        long chatId = generateChatId();
        ChatData chatData = new ChatData();
        chatData.setId(chatId);

        // when
        chatRepository.add(chatData);

        // then
        assertResult(chatId);
    }

    @Test
    void addExistsChat_ThrowsDuplicateUniqueFieldException() {
        // given
        long chatId = generateChatId();
        ChatData chatData = new ChatData();
        chatData.setId(chatId);
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatId);

        // then/when
        assertThrows(DuplicateUniqueFieldException.class, () -> chatRepository.add(chatData));

        // when
        assertResult(chatId);
        jdbcTemplate.update("DELETE FROM chats WHERE id=?", chatId);
    }

    @ParameterizedTest
    @NullSource
    void addNullChatId_ThrowsBadEntityException(Long chatId) {
        // given
        ChatData chatData = new ChatData();
        chatData.setId(chatId);

        // then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(chatData));

        // when
        assertTrue(checkMissingData(chatId));
    }

    @Test
    void addNullChat_ThrowsBadEntityException() {
        // given
        ChatData chatData = null;

        // then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(chatData));
    }

    @Test
    @Transactional
    @Rollback
    void removeExistsChatId_OK () {
        // given
        long chatId = generateChatId();
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatId);

        // when
        chatRepository.remove(chatId);

        // then
        assertTrue(checkMissingData(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistsChatId_OK () {
        // given
        long chatId = generateChatId();

        // when
        chatRepository.remove(chatId);

        // then
        assertTrue(checkMissingData(chatId));
    }

    private long generateChatId() {
        long chatId;
        do {
            chatId = new Random().nextLong();
        } while (!checkMissingData(chatId));
        return chatId;
    }

    private boolean checkMissingData(Long chatId) {
        Integer result = jdbcTemplate.queryForObject("SELECT count(*) FROM chats where id = ?", Integer.class, chatId);
        return result == null || result == 0;
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
