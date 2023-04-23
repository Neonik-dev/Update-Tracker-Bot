package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;

import java.util.List;

@RequiredArgsConstructor
public class ChatLinkServiceImpl implements ChatLinkService {
    private final ChatLinkRepository chatLinkRepository;
    @Override
    public List<Long> getAllLink(long chatId) {
        List<ChatLinkData> arrChatLink = chatLinkRepository.getAllByChatId(chatId);
        return arrChatLink.stream().map(ChatLinkData::getLinkId).toList();
    }

    @Override
    public List<Long> getAllChat(long linkId) {
        List<ChatLinkData> arrChatLink = chatLinkRepository.getAllChatByLink(linkId);
        return arrChatLink.stream().map(ChatLinkData::getLinkId).toList();
    }
}
