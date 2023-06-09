package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chats.CHATS;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    @Override
    public void add(Chat chatData) throws DuplicateUniqueFieldException, BadEntityException {
        checkEntity(chatData);
        dsl.insertInto(CHATS, CHATS.ID, CHATS.CREATED_DATE, CHATS.LAST_CALL_DATE)
                .values(chatData.getId(), chatData.getCreatedDate().toLocalDateTime(), chatData.getLastCallDate())
                .execute();
    }

    @Override
    public void remove(Long id) {
        checkChatId(id);
        dsl.delete(CHATS).where(CHATS.ID.eq(id)).execute();
    }

    @Override
    public List<Chat> findAll() {
        return dsl.select(CHATS.fields())
                .from(CHATS)
                .fetch()
                .into(Chat.class);
    }
}
