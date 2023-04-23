package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.Chats;

@Repository
public interface JpaChatRepository extends JpaRepository<Chats, Long> {
}
