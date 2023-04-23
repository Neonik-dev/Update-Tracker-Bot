package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.JdbcUtils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatLinkRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JdbcChatLinkRepository.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcChatLinkRepositoryIT extends IntegrationEnvironment {
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ChatLinkData> rowMapper = new DataClassRowMapper<>(ChatLinkData.class);
    private final JdbcUtils utils;
    private ChatLinkData chatLinkData;
    private ChatData chatData;
    private LinkData linkData;
    private DomainData domainData;

    @BeforeEach
    public void initChatLinkData() {
        chatData = utils.createChatData();
        domainData = utils.createDomainData();
        linkData = utils.createLinkData();
        linkData.setDomainId(domainData.getId());

        chatLinkData = ChatLinkData.builder()
                .linkId(linkData.getId())
                .chatId(chatData.getId())
                .build();
    }

    @Test
    @Transactional
    @Rollback
    void addUniqueChatLink_OK() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);

        // when
        chatLinkRepository.add(chatLinkData);

        // then
        ChatLinkData result = jdbcTemplate.queryForObject(
                "SELECT * FROM chat_link WHERE chat_id=? AND link_id=?",
                rowMapper,
                chatLinkData.getChatId(),
                chatLinkData.getLinkId()
        );
        utils.assertChatLinkResult(result, chatLinkData);
    }

    @Test
    @Transactional
    @Rollback
    void addChatLinkNotExistsLinkId_ThrowsDataIntegrityViolationException() {
        // given
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());

        // when/then
        assertThrows(DataIntegrityViolationException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @Transactional
    @Rollback
    void addDuplicateChatLink_ThrowsDataIntegrityViolationException() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);
        chatLinkRepository.add(chatLinkData);

        // when/then
        assertThrows(DataIntegrityViolationException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @Transactional
    @Rollback
    void addNullChatLink_ThrowsBadEntityException() {
        // given
        chatLinkData = null;

        // when/then
        assertThrows(BadEntityException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @Transactional
    @Rollback
    void addEmptyChatLink_ThrowsBadEntityException() {
        // given
        chatLinkData = ChatLinkData.builder().build();

        // when/then
        assertThrows(BadEntityException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @Transactional
    @Rollback
    void removeChatLink_OK() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);
        chatLinkRepository.add(chatLinkData);

        // when
        chatLinkRepository.remove(chatLinkData.getChatId(), chatLinkData.getLinkId());

        // then
        assertTrue(utils.checkMissingDataChatLink(chatLinkData.getChatId(), chatLinkData.getLinkId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistsChatLink_OK() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);

        // when
        chatLinkRepository.remove(chatLinkData.getChatId(), chatLinkData.getLinkId());

        // then
        assertTrue(utils.checkMissingDataChatLink(chatLinkData.getChatId(), chatLinkData.getLinkId()));
    }
}
