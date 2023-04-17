package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatLinkRepositoryImpl implements ChatLinkRepository {
    private final JdbcTemplate template;
    private final RowMapper<ChatLinkData> rowMapper = new DataClassRowMapper<>(ChatLinkData.class);

    private static final String INSERT_QUERY = "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)";
    private static final String DELETE_BY_CHAT_ID_AND_LINK_ID_QUERY = "DELETE FROM chat_link WHERE chat_id=? AND link_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM chat_link";
    private static final String SELECT_BY_CHAT_ID = "SELECT * FROM chat_link WHERE chat_id=?";
    private static final String SELECT_BY_LINK_ID = "SELECT * FROM chat_link WHERE link_id=?";

    private void checkEntity(ChatLinkData chatLinkData) throws BadEntityException {
        if (chatLinkData == null || chatLinkData.getChatId() == null || chatLinkData.getLinkId() == null)
            throw new BadEntityException();
    }

    @Override
    public void add(ChatLinkData chatLinkData) throws BadEntityException {
        checkEntity(chatLinkData);
        template.update(INSERT_QUERY, chatLinkData.getChatId(), chatLinkData.getLinkId());
    }

    @Override
    public void remove(long chatId, long linkId) {
        template.update(DELETE_BY_CHAT_ID_AND_LINK_ID_QUERY, chatId, linkId);
    }

    @Override
    public List<ChatLinkData> findAll() {
        return template.query(SELECT_ALL_QUERY, rowMapper);
    }

    @Override
    public List<ChatLinkData> findAllByChatId(long chatId) {
        return template.query(SELECT_BY_CHAT_ID, rowMapper, chatId);
    }

    @Override
    public List<ChatLinkData> getAllChatByLink(long linkId) {
        return template.query(SELECT_BY_LINK_ID, rowMapper, linkId);
    }
}
