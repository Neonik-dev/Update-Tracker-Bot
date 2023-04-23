package ru.tinkoff.edu.java.database.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.JdbcUtils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chats.CHATS;

@JooqTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JooqChatRepository.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JooqChatRepositoryIT extends IntegrationEnvironment{
    private final DSLContext dsl;
    private final JooqChatRepository chatRepository;
    private final JdbcUtils utils;
    private ChatData chatData;

    @BeforeEach
    public void initChatData() {
        chatData = utils.createChatData();
    }

    @Test
    void addUniqueChat_OK() {
        // given

        // when
        chatRepository.add(chatData);

        // then
        ChatData result = dsl.select(CHATS.ID, CHATS.LAST_CALL_DATE)
                .from(CHATS)
                .where(CHATS.ID.eq(chatData.getId()))
                .fetchOne()
                .map(
                        record -> ChatData.builder()
                                .id(record.getValue(CHATS.ID))
                                .lastCallDate(record.getValue(CHATS.LAST_CALL_DATE))
                                .build()
                );
        utils.assertChatResult(result, chatData);
    }

    @Test
    void addExistsChat_ThrowsDuplicateKeyException() {
        // given
        chatRepository.add(chatData);

        // then/when
        assertThrows(DuplicateKeyException.class, () -> chatRepository.add(chatData));
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
    void removeExistsChatId_OK () {
        // given
        chatRepository.add(chatData);

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    @Test
    void removeNotExistsChatId_OK () {
        // given

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }
}
