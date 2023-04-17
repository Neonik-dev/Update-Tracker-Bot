package ru.tinkoff.edu.java.database.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.*;
import ru.tinkoff.edu.java.scrapper.persistence.repository.impl.ChatLinkRepositoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {ChatLinkRepositoryImpl.class, DBConfiguration.class, TestConfiguration.class, JdbcUtils.class})
public class JdbcChatLinkIT extends IntegrationEnvironment {
    private final ChatLinkRepositoryImpl chatLinkRepository;
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

        chatLinkData = new ChatLinkData();
        chatLinkData.setLinkId(linkData.getId());
        chatLinkData.setChatId(chatData.getId());
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addUniqueChatLink_OK() {
        // given
        initEnvironment();

        // when
        chatLinkRepository.add(chatLinkData);

        // then
        ChatLinkData result = jdbcTemplate.queryForObject(
                "SELECT * FROM chat_link WHERE chat_id=? AND link_id=?",
                rowMapper,
                chatLinkData.getChatId(),
                chatLinkData.getLinkId()
        );
        assertNotNull(result);
        assertAll(
                () -> assertEquals(result.getChatId(), chatLinkData.getChatId()),
                () -> assertEquals(result.getLinkId(), chatLinkData.getLinkId())
        );
    }

    @Test
    @Transactional
    @Rollback
    void addChatLinkNotExistsLinkId_ThrowsForeignKeyNotExistsException() {
        // given
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());

        // when/then
        assertThrows(ForeignKeyNotExistsException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void addDuplicateChatLink_ThrowsDuplicateUniqueFieldException() {
        // given
        initEnvironment();
        chatLinkRepository.add(chatLinkData);

        // when/then
        assertThrows(DuplicateUniqueFieldException.class, () -> chatLinkRepository.add(chatLinkData));
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
        chatLinkData = new ChatLinkData();

        // when/then
        assertThrows(BadEntityException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void removeChatLink_OK() {
        // given
        initEnvironment();
        chatLinkRepository.add(chatLinkData);

        // when
        chatLinkRepository.remove(chatLinkData.getChatId(), chatLinkData.getLinkId());

        // then
        assertTrue(utils.checkMissingDataChatLink(chatLinkData.getChatId(), chatLinkData.getLinkId()));
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback
    void removeNotExistsChatLink_OK() {
        // given
        initEnvironment();

        // when
        chatLinkRepository.remove(chatLinkData.getChatId(), chatLinkData.getLinkId());

        // then
        assertTrue(utils.checkMissingDataChatLink(chatLinkData.getChatId(), chatLinkData.getLinkId()));
    }

    private void initEnvironment() {
        jdbcTemplate.update("INSERT INTO domains(id, name) VALUES (?, ?)", domainData.getId(), domainData.getName());
        jdbcTemplate.update("INSERT INTO chats(id) VALUES (?)", chatData.getId());
        jdbcTemplate.update(
                "INSERT INTO links(id, link, domain_id, data_changes) VALUES (?, ?, ?, ?)",
                linkData.getId(),
                linkData.getLink(),
                linkData.getDomainId(),
                new ConvertorFromMapToJson().convertToDatabaseColumn(linkData.getDataChanges())
        );
    }


}
