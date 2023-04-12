package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;

import java.util.List;

public interface DomainRepository {
    void add(DomainData domain);
    void remove(long id);
    void remove(String name);
    List<DomainData> findAll();
}
