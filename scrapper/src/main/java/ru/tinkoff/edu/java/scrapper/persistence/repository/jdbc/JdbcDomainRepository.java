package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;

import java.util.List;

@RequiredArgsConstructor
public class JdbcDomainRepository implements DomainRepository {
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM domains WHERE name=?";
    private static final String INSERT_QUERY = "INSERT INTO domains(name) VALUES (?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM domains WHERE id=?";
    private static final String DELETE_BY_NAME_QUERY = "DELETE FROM domains WHERE name=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM domains";
    private final JdbcTemplate template;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);

    @Override
    public DomainData getByName(String name) {
        return template.queryForObject(SELECT_BY_NAME_QUERY, rowMapper, name);
    }

    @Override
    public void add(DomainData domainData) {
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
