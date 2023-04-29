package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Domain;

public interface JpaDomainRepository extends JpaRepository<Domain, Long> {
    @Query("SELECT d from Domain d where d.name = :domain")
    Domain findByName(String domain);
}
