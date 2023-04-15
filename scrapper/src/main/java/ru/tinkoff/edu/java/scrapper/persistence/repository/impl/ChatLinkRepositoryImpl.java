package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatLinkRepositoryImpl implements ChatLinkRepository {
    private final JdbcTemplate template;
    private final RowMapper<ChatLinkData> rowMapper = new DataClassRowMapper<>(ChatLinkData.class);

    private void checkEntity(ChatLinkData chatLinkData) throws BadEntityException {
        if (chatLinkData == null || chatLinkData.getChatId() == null || chatLinkData.getLinkId() == null)
            throw new BadEntityException();
    }
    @Override
    public void add(ChatLinkData chatLinkData) throws DuplicateUniqueFieldException, BadEntityException, ForeignKeyNotExistsException {
        checkEntity(chatLinkData);
        try {
            template.update(
                    "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)",
                    chatLinkData.getChatId(),
                    chatLinkData.getLinkId()
            );
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("У пользователя уже отслуживается данная ссылка");
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyNotExistsException("Отсутствует внешний ключ chat_id/link_id");
        }
    }

    @Override
    public void remove(long chatId, long linkId) {
        template.update("DELETE FROM chat_link WHERE chat_id=? AND link_id=?", chatId, linkId);
    }

    @Override
    public List<ChatLinkData> findAll() {
        return template.query("SELECT * FROM chat_link", rowMapper);
    }

    @Override
    public List<ChatLinkData> findAllByChatId(long chatId) {
        return template.query("SELECT * FROM chat_link WHERE chat_id=?", rowMapper, chatId);
    }
}
