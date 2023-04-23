package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.DomainData;

import java.util.List;

public interface DomainRepository {
     default void checkEntity(DomainData domainData) throws BadEntityException {
        if (domainData == null || domainData.getName() == null)
            throw new BadEntityException();
    }
    void add(DomainData domainData);
    void remove(long id);
    void remove(String name);
    DomainData getByName(String name);
    List<DomainData> findAll();
}
