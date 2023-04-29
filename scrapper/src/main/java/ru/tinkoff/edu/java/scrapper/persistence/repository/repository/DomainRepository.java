package ru.tinkoff.edu.java.scrapper.persistence.repository.repository;

import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Domain;

import java.util.List;

public interface DomainRepository {
     default void checkEntity(Domain domainData) throws BadEntityException {
        if (domainData == null || domainData.getName() == null)
            throw new BadEntityException();
    }
    void add(Domain domainData);
    void remove(long id);
    void remove(String name);
    Domain getByName(String name);
    List<Domain> findAll();
}
