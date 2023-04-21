package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatRepository;

import java.util.List;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chats.CHATS;

@Primary
@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    @Override
    public void add(ChatData chatData) throws DuplicateUniqueFieldException, BadEntityException {
        checkEntity(chatData);
        dsl.insertInto(CHATS, CHATS.ID).values(chatData.getId()).execute();
    }

    @Override
    public void remove(long id) {
        dsl.delete(CHATS).where(CHATS.ID.eq(id)).execute();
    }

    @Override
    public List<ChatData> findAll() {
        return dsl.select(CHATS.fields())
                .from(CHATS)
                .fetch()
                .into(ChatData.class);
    }
}
