package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcChatLinkService implements ChatLinkService {
    private final ChatLinkRepository chatLinkRepository;
    @Override
    public List<Long> getAllLink(long chatId) {
        List<ChatLinkData> arrChatLink = chatLinkRepository.findAllByChatId(chatId);
        return arrChatLink.stream().map(ChatLinkData::getLinkId).toList();
    }

    @Override
    public List<Long> getAllChat(long linkId) {
        List<ChatLinkData> arrChatLink = chatLinkRepository.getAllChatByLink(linkId);
        return arrChatLink.stream().map(ChatLinkData::getLinkId).toList();
    }
}
