package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;

import java.util.List;

@RequiredArgsConstructor
public class JdbcChatLinkService implements ChatLinkService {
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public List<Long> getAllLink(long chatId) {
        List<ChatLink> arrChatLink = chatLinkRepository.getAllByChatId(chatId);
        return arrChatLink.stream().map(ChatLink::getLinkId).toList();
    }

    @Override
    public List<Long> getAllChat(long linkId) {
        List<ChatLink> arrChatLink = chatLinkRepository.getAllChatByLink(linkId);
        return arrChatLink.stream().map(ChatLink::getLinkId).toList();
    }
}
