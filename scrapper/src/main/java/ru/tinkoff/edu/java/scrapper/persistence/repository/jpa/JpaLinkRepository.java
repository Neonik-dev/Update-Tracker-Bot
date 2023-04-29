package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    @Query("SELECT l FROM Link l where l.link = :link")
    Optional<Link> findByLink(String link);
}
