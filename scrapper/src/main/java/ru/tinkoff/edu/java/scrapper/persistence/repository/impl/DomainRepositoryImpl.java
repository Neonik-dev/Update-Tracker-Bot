package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DomainRepositoryImpl implements DomainRepository {
    private final JdbcTemplate template;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);

    void checkEntity(DomainData domainData) throws BadEntityException {
        if (domainData == null || domainData.getName() == null)
            throw new BadEntityException();
    }

    @Override
    public DomainData getByName(String name) throws EmptyResultException {
        try {
            return template.queryForObject("SELECT * FROM domains WHERE name=?", rowMapper, name);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException("Программа пока не может отслеживать ссылки с доменом " + name);
        }
    }

    @Override
    public void add(DomainData domainData) throws BadEntityException, DuplicateUniqueFieldException {
        try {
            checkEntity(domainData);
            template.update("INSERT INTO domains(name) VALUES (?)", domainData.getName());
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("Имя/id домена уже существует");
        }
    }

    @Override
    public void remove(long id) {
        template.update("DELETE FROM domains WHERE id=?", id);
    }

    @Override
    public void remove(String name) {
        template.update("DELETE FROM domains WHERE name=?", name);
    }

    @Override
    public List<DomainData> findAll() {
        return template.query("SELECT * FROM domains", rowMapper);
    }
}
