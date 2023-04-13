package ru.tinkoff.edu.java.scrapper.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ConvertorFromMapToJson;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {
    private final JdbcTemplate template;
    private final RowMapper<LinkData> rowMapper = new DataClassRowMapper<>(LinkData.class);

    void checkEntity(LinkData linkData) throws BadEntityException {
        if (linkData == null || linkData.getLink() == null
                || linkData.getDataChanges() == null || linkData.getDomainId() == null)
            throw new BadEntityException();
    }

    @Override
    public void add(LinkData link) throws BadEntityException, ForeignKeyNotExistsException, DuplicateUniqueFieldException {
        try {
            checkEntity(link);
            template.update(
                    "INSERT INTO links(link, domain_id, data_changes) VALUES (?, ?, ?)",
                    link.getLink(),
                    link.getDomainId(),
                    new ConvertorFromMapToJson().convertToDatabaseColumn(link.getDataChanges())
            );
        }  catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("Такой id/link в таблице links уже существует");
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyNotExistsException(
                    String.format("Ключ (domain_id)=(%d) отсутствует в таблице \"domains\"", link.getDomainId())
            );
        }
    }

    @Override
    public void remove(long id) {
        template.update("DELETE FROM links WHERE id=?", id);
    }

    @Override
    public void remove(String link) {
        template.update("DELETE FROM links WHERE link=?", link);
    }

    @Override
    public List<LinkData> findAll() {
        return template.query("SELECT * FROM links", rowMapper);
    }
}
