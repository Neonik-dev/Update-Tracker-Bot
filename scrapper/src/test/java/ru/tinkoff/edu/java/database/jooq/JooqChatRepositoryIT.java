package ru.tinkoff.edu.java.database.jooq;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;

import static org.junit.jupiter.api.Assertions.*;

@JooqTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JooqChatRepository.class, DBConfiguration.class, Utils.class})
public class JooqChatRepositoryIT extends IntegrationEnvironment {
    private final JooqChatRepository chatRepository;
    private final Utils utils;
    private Chat chatData;

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
        utils.assertChatResult(chatData);
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
        // given/then/when
        assertThrows(BadEntityException.class, () -> chatRepository.add(null));
    }

    @Test
    void removeExistsChatId_OK() {
        // given
        chatRepository.add(chatData);

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    @Test
    void removeNotExistsChatId_OK() {
        // given

        // when
        chatRepository.remove(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    @Test
    void removeNullChatId_ThrowsBadEntityException() {
        // given/when/then
        assertThrows(
                BadEntityException.class,
                () -> chatRepository.remove(null)
        );
    }
}
