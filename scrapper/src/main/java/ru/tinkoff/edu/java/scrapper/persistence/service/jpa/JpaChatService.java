package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public void register(Long tgChatId) {
        try {
            if (chatRepository.existsById(tgChatId)) {
                throw new DuplicateUniqueFieldException(
                        String.format("Такой (chat_id)=(%s) уже зарегистрирован", tgChatId)
                );
            }

            Chat chat = new Chat(tgChatId, OffsetDateTime.now(), LocalDate.now());
            chatRepository.save(chat);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new BadEntityException();
        }
    }

    @Override
    @Transactional
    public void unregister(Long tgChatId) {
        try {
            chatRepository.deleteById(tgChatId);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new BadEntityException();
        } catch (EmptyResultDataAccessException e) {
            log.warn(String.format("Запрос на удаление чата (chat_id)=(%s), хотя он не был зарегистрирован", tgChatId));
        }
    }
}
