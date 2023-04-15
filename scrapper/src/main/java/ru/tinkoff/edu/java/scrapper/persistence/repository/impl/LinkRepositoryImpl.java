package ru.tinkoff.edu.java.scrapper.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ConvertorFromMapToJson;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {
    private final JdbcTemplate template;
    private final RowMapper<LinkData> rowMapper = (rs, rowNum) -> {
        LinkData linkDataBD = new LinkData();
        linkDataBD.setId(rs.getLong(1));
        linkDataBD.setLink(rs.getString(2));
        linkDataBD.setDomainId(rs.getLong(3));
        linkDataBD.setDataChanges(new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(4)));
        return linkDataBD;
    };

    void checkEntity(LinkData linkData) throws BadEntityException {
        if (linkData == null || linkData.getLink() == null
                || linkData.getDataChanges() == null || linkData.getDomainId() == null)
            throw new BadEntityException();
    }

    @Override
    @Transactional
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
    public LinkData getOldestUpdateLink() throws EmptyResultException {
        try {
            return template.queryForObject(
                    "SELECT id, link, domain_id, data_changes, page_updated_date FROM links ORDER BY scheduler_updated_date LIMIT 1",
                    (rs, rowNum) -> {
                        LinkData linkDataBD = new LinkData();
                        linkDataBD.setId(rs.getLong(1));
                        linkDataBD.setLink(rs.getString(2));
                        linkDataBD.setDomainId(rs.getLong(3));
                        linkDataBD.setDataChanges(new ConvertorFromMapToJson().convertToEntityAttribute((PGobject) rs.getObject(4)));
                        linkDataBD.setPageUpdateDate(new Date(rs.getDate(5).getTime()).toInstant().atOffset(ZoneOffset.UTC));
                        return linkDataBD;
                    });
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException("Ещё нет ни одной ссылки");
        }
    }

    @Override
    public void updateUpdatedDateLink(long linkId) {
        template.update("UPDATE links SET scheduler_updated_date = now() WHERE id = ?", linkId);
    }

    public LinkData getByLink(String link) throws EmptyResultException {
        try {
            return template.queryForObject("SELECT id, link, domain_id, data_changes FROM links WHERE link=?", rowMapper, link);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException(String.format("Ссылка (%s) отсутвствует в базе данных", link));
        }
    }

    @Override
    public List<LinkData> findAll() {
        return template.query("SELECT * FROM links", rowMapper);
    }

    @Override
    public List<LinkData> getByLinkIds(List<Long> arrChatLink) {
        String inSql = String.join(",", Collections.nCopies(arrChatLink.size(), "?"));
        return template.query(
                String.format("SELECT id, link, domain_id, data_changes FROM links WHERE id IN (%s)", inSql),
                rowMapper,
                arrChatLink.toArray()
        );
    }
}
