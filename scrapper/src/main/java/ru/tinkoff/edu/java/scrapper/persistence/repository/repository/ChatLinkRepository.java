package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatLinkData;

import java.util.Collection;
import java.util.List;

public interface ChatLinkRepository {
    default void checkEntity(ChatLinkData chatLinkData) throws BadEntityException {
        if (chatLinkData == null || chatLinkData.getChatId() == null || chatLinkData.getLinkId() == null)
            throw new BadEntityException();
    }
    void add(ChatLinkData chatLinkData);
    void remove(long chatId, long linkId);
    Collection<ChatLinkData> findAll();
    List<ChatLinkData> getAllByChatId(long chatId);
    List<ChatLinkData> getAllChatByLink(long linkId);
}
