package ru.tinkoff.edu.java.database.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.IntegrationEnvironment;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.*;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatLinkRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHATS;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;

@JooqTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {JooqChatLinkRepository.class, DBConfiguration.class, Utils.class})
public class JooqChatLinkRepositoryIT extends IntegrationEnvironment {
    private final DSLContext dsl;
    private final JooqChatLinkRepository chatLinkRepository;
    private final Utils utils;
    private ChatLink chatLinkData;
    private Chat chatData;
    private Link linkData;
    private Domain domainData;

    @BeforeEach
    public void initChatLinkData() {
        chatData = utils.createChatData();
        domainData = utils.createDomainData();
        linkData = utils.createLinkData();
        linkData.setDomain(domainData);

        chatLinkData = new ChatLink(chatData.getId(), linkData.getId());
    }

    @Test
    void addUniqueChatLink_OK() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);

        // when
        chatLinkRepository.add(chatLinkData);

        // then
        ChatLink result = new ChatLink(
                dsl.select(CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
                        .from(CHAT_LINK)
                        .where(
                                CHAT_LINK.CHAT_ID.eq(chatLinkData.getChatId())
                        )
                        .and(
                                CHAT_LINK.LINK_ID.eq(chatLinkData.getLinkId())
                        )
                        .fetchOneInto(ChatLinkPK.class)
        );
        utils.assertChatLinkResult(result, chatLinkData);
    }

    @Test
    void addChatLinkNotExistsLinkId_ThrowsDataIntegrityViolationException() {
        // given
        dsl.insertInto(CHATS, CHATS.ID).values(chatData.getId()).execute();

        // when/then
        assertThrows(DataIntegrityViolationException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    void addDuplicateChatLink_ThrowsDataIntegrityViolationException() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);
        chatLinkRepository.add(chatLinkData);

        // when/then
        assertThrows(DataIntegrityViolationException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    void addNullChatLink_ThrowsBadEntityException() {
        // given
        chatLinkData = null;

        // when/then
        assertThrows(BadEntityException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
    void addEmptyChatLink_ThrowsBadEntityException() {
        // given
        chatLinkData = new ChatLink();

        // when/then
        assertThrows(BadEntityException.class, () -> chatLinkRepository.add(chatLinkData));
    }

    @Test
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
    void removeNotExistsChatLink_OK() {
        // given
        utils.initEnvironmentChatLink(domainData, chatData, linkData);

        // when
        chatLinkRepository.remove(chatLinkData.getChatId(), chatLinkData.getLinkId());

        // then
        assertTrue(utils.checkMissingDataChatLink(chatLinkData.getChatId(), chatLinkData.getLinkId()));
    }
}
