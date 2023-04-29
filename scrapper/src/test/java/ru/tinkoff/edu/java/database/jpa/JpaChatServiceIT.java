package ru.tinkoff.edu.java.database.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ScrapperApplication.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {Utils.class, DBConfiguration.class})
public class JpaChatServiceIT extends IntegrationEnvironment {
    private final JpaChatService chatService;
    private final JpaChatRepository chatRepository;
    private final Utils utils;
    private Chat chatData;

    @BeforeEach
    public void initChatData() {
        chatData = utils.createChatData();
    }

    @Test
    @Transactional
    @Rollback
    public void addUniqueChat_OK() {
        // given

        // when
        chatService.register(chatData.getId());
        chatRepository.flush();

        // then
        utils.assertChatResult(chatData);
    }

    @Test
    @Transactional
    @Rollback
    public void addExistsChat_ThrowsDuplicateUniqueFieldException() {
        // given
        chatService.register(chatData.getId());
        chatRepository.flush();

        // when/then
        assertThrows(
                DuplicateUniqueFieldException.class,
                () -> chatService.register(chatData.getId())
        );
    }

    @Test
    @Transactional
    @Rollback
    public void addNullChatId_ThrowsBadEntityException() {
        // given/when/then
        assertThrows(
                BadEntityException.class,
                () -> chatService.register(null)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeExistsChatId_OK () {
        // given
        chatService.register(chatData.getId());

        // when
        chatService.unregister(chatData.getId());

        // then
        assertTrue(utils.checkMissingDataChat(chatData.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistsChatId_OK () {
        // given

        // when
        chatService.unregister(chatData.getId());

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
                () -> chatService.unregister(null)
        );
    }
}
