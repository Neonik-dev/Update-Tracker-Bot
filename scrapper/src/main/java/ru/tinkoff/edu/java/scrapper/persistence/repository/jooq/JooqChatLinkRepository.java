package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;


import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;

import java.util.Collection;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.ChatLink.CHAT_LINK;

@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext dsl;

    @Override
    public void add(ChatLink chatLinkData) {
        checkEntity(chatLinkData);
        dsl.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
                .values(chatLinkData.getChatId(), chatLinkData.getLinkId())
                .execute();

    }

    @Override
    public void remove(long chatId, long linkId) {
        dsl.delete(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(chatId))
                .and(CHAT_LINK.LINK_ID.eq(linkId))
                .execute();
    }

    @Override
    public Collection<ChatLink> findAll() {
        return dsl.select(CHAT_LINK.fields())
                .from(CHAT_LINK)
                .fetch()
                .into(ChatLink.class);
    }

    @Override
    public List<ChatLink> getAllByChatId(long chatId) {
        return dsl.select(CHAT_LINK.fields())
                .from(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(chatId))
                .fetch()
                .into(ChatLink.class);
    }

    @Override
    public List<ChatLink> getAllChatByLink(long linkId) {
        return dsl.select(CHAT_LINK.fields())
                .from(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .fetch()
                .into(ChatLink.class);
    }
}
