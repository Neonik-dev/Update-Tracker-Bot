package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JdbcChatRepository.class, DBConfiguration.class, TestConfiguration.class, Utils.class})
public class JdbcChatRepositoryIT extends IntegrationEnvironment {
    private final JdbcChatRepository chatRepository;
    private final Utils utils;
    private Chat chatData;

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
        utils.assertChatResult(chatData);
    }

    @Test
    @Transactional
    @Rollback
    void addExistsChat_ThrowsDuplicateKeyException() {
        // given
        chatRepository.add(chatData);

        // then/when
        assertThrows(DuplicateKeyException.class, () -> chatRepository.add(chatData));
    }

    @Test
    @Transactional
    @Rollback
    void addNullChatId_ThrowsBadEntityException() {
        // given
        chatData.setId(null);

        // then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(chatData));
    }

    @Test
    @Transactional
    @Rollback
    void addNullChat_ThrowsBadEntityException() {
        // given/then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(null));
    }

    @Test
    @Transactional
    @Rollback
    void removeExistsChatId_OK () {
        // given
        chatRepository.add(chatData);

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

    @Test
    @Transactional
    @Rollback
    void removeNullChatId_ThrowsBadEntityException() {
        // given/when/then
        assertThrows(
                BadEntityException.class,
                () -> chatRepository.remove(null)
        );
    }
}
