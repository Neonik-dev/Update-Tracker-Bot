package ru.tinkoff.edu.java.scrapper.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DomainRepositoryImpl implements DomainRepository {
    private final JdbcTemplate template;
    private final RowMapper<DomainData> rowMapper = new DataClassRowMapper<>(DomainData.class);

    @Override
    public void add(DomainData domain) {
        template.update("INSERT INTO domains(name) VALUES (?)", domain.getName());
        printAll();
    }

    private void printAll() {
        findAll().forEach(System.out::println);
    }

    @Override
    public void remove(long id) {
        template.update("DELETE FROM domain WHERE id=?", id);
        printAll();
    }

    @Override
    public void remove(String name) {
        template.update("DELETE FROM domain WHERE name=?", name);
        printAll();
    }

    @Override
    public List<DomainData> findAll() {
        return template.query("SELECT * FROM domains", rowMapper);
    }
}
