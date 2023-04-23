package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.ChatLink;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.ChatLinkPK;

import java.util.List;

@Repository
public interface JpaChatLinkRepository extends JpaRepository<ChatLink, ChatLinkPK> {
    @Query("SELECT t.id.linkId FROM ChatLink t where t.id.chatId = :chatId")
    List<Long> findAllByChatId(Long chatId);
}
