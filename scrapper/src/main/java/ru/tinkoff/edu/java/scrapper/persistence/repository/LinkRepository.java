package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;

import java.util.List;

public interface LinkRepository {
    void add(LinkData link);
    void remove(long id);
    void remove(String link);
    List<LinkData> findAll();
}
