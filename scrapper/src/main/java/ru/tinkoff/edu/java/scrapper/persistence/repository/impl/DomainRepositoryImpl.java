package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DomainRepositoryImpl implements DomainRepository {
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM domains WHERE name=?";
    private static final String INSERT_QUERY = "INSERT INTO domains(name) VALUES (?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM domains WHERE id=?";
    private static final String DELETE_BY_NAME_QUERY = "DELETE FROM domains WHERE name=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM domains";
    private final JdbcTemplate template;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);

    void checkEntity(DomainData domainData) throws BadEntityException {
        if (domainData == null || domainData.getName() == null)
            throw new BadEntityException();
    }

    @Override
    public DomainData getByName(String name) {
        return template.queryForObject(SELECT_BY_NAME_QUERY, rowMapper, name);
    }

    @Override
    public void add(DomainData domainData) throws BadEntityException {
        checkEntity(domainData);
        template.update(INSERT_QUERY, domainData.getName());
    }

    @Override
    public void remove(long id) {
        template.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void remove(String name) {
        template.update(DELETE_BY_NAME_QUERY, name);
    }

    @Override
    public List<DomainData> findAll() {
        return template.query(SELECT_ALL_QUERY, rowMapper);
    }
}
