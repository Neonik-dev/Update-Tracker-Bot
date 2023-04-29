package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
