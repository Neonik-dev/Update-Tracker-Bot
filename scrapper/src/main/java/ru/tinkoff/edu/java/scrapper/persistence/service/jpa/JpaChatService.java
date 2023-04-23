package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.Chats;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Primary
@Service
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;
    @Override
    @Transactional
    public void register(Long tgChatId) {
        System.out.println("Hello world x2");
        Chats chat = new Chats();
        chat.setId(tgChatId);
        chat.setLastCallDate(LocalDate.now());
        chat.setCreatedDate(OffsetDateTime.now());
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void unregister(Long tgChatId) {
        chatRepository.deleteById(tgChatId);
    }
}
