package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.Links;

@Repository
public interface JpaLinkRepository extends JpaRepository<Links, Long> {
    @Query("SELECT l FROM Links l where l.link = :link")
    Links findByLink(String link);
}
