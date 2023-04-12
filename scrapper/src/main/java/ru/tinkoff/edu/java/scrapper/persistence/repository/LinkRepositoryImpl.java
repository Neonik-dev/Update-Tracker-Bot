package ru.tinkoff.edu.java.scrapper.persistence.repository;

import io.swagger.v3.oas.models.links.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.domain.MyConvertor;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {
    private final JdbcTemplate template;
    private final RowMapper<LinkData> rowMapper = new DataClassRowMapper<>(LinkData.class);
    @Override
    public void add(LinkData link) {
        String queryText = "INSERT INTO links(link, domain_id, data_changes) VALUES (?, ?, ?)";
        System.out.println("ubed");
        System.out.println(link.getDomainId());
        template.update(queryText, link.getLink(), link.getDomainId(), new MyConvertor().convertToDatabaseColumn(link.getDataChanges()));
        printAll();
    }

    @Override
    public void remove(long id) {
        template.update("DELETE FROM links WHERE id=?", id);
        printAll();
    }

    @Override
    public void remove(String link) {
        template.update("DELETE FROM links WHERE link=?", link);
        printAll();
    }

    @Override
    public List<LinkData> findAll() {
        return template.query("SELECT * FROM links", rowMapper);
    }

    private void printAll() {
        findAll().forEach(System.out::println);
    }
}
