package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(Long tgChatId) {
        Chat chatData = new Chat(tgChatId, OffsetDateTime.now(), LocalDate.now());
        try {
            chatRepository.add(chatData);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("Пользователь с таким id уже существует");
        }
    }

    @Override
    public void unregister(Long tgChatId) {
        chatRepository.remove(tgChatId);
    }
}
