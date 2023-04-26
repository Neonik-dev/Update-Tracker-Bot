package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;

import java.util.Collection;
import java.util.List;

public interface ChatLinkRepository {
    default void checkEntity(ChatLink chatLinkData) throws BadEntityException {
        if (chatLinkData == null ||
            chatLinkData.getId() == null ||
            chatLinkData.getChatId() == null ||
            chatLinkData.getLinkId() == null
        )
            throw new BadEntityException();
    }
    void add(ChatLink chatLinkData);
    void remove(long chatId, long linkId);
    Collection<ChatLink> findAll();
    List<ChatLink> getAllByChatId(long chatId);
    List<ChatLink> getAllChatByLink(long linkId);
}
