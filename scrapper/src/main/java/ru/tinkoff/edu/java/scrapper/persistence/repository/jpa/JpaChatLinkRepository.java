package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkPK;

import java.util.List;

public interface JpaChatLinkRepository extends JpaRepository<ChatLink, ChatLinkPK> {
    @Query(value = "SELECT link_id FROM chat_link t where link_id = :chatId", nativeQuery = true)
    List<Long> findAllByChatId(Long chatId);
    @Query(value = "SELECT chat_id FROM chat_link t where link_id = :linkId", nativeQuery = true)
    List<Long> findAllByLinkId(Long linkId);
}
