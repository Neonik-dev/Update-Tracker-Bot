package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jpa.Domains;

public interface JpaDomainRepository extends JpaRepository<Domains, Long> {
    @Query("SELECT d from Domains d where d.name = :domain")
    Domains findByName(String domain);
}
