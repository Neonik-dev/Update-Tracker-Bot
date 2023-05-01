package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;

import java.util.List;

@RequiredArgsConstructor
public class JpaChatLinkService implements ChatLinkService {
    private final JpaChatLinkRepository chatLinkRepository;
    @Override
    public List<Long> getAllLink(long chatId) {
        return chatLinkRepository.findAllByChatId(chatId);
    }

    @Override
    public List<Long> getAllChat(long linkId) {
        return chatLinkRepository.findAllByLinkId(linkId);
    }
}
