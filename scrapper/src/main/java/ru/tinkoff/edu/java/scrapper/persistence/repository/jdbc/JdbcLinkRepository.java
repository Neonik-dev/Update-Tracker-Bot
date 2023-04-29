package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate template;
    private static final ConvertorFromMapToJson CONVERTOR = new ConvertorFromMapToJson();
    private final RowMapper<Link> rowMapper = (rs, rowNum) -> new Link(
            rs.getLong(1),
            rs.getString(2),
            null,
            new Timestamp(rs.getTimestamp(3).getTime()).toLocalDateTime().atOffset(ZoneOffset.UTC),
            null,
            rs.getLong(4),
            CONVERTOR.convertToEntityAttribute((PGobject) rs.getObject(5))
    );
    private static final String INSERT_QUERY = "INSERT INTO links(link, domain_id, page_updated_date, data_changes) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM links WHERE id=?";
    private static final String DELETE_BY_LINK_QUERY = "DELETE FROM links WHERE link=?";
    private static final String UPDATE_LATEST_SCHEDULER_DATE_QUERY = "UPDATE links SET scheduler_updated_date = now() WHERE id = ?";
    private static final String SELECT_BY_LINK_QUERY = "SELECT id, link, page_updated_date, domain_id, data_changes FROM links WHERE link=?";
    private static final String SELECT_ALL_QUERY = "SELECT id, link, page_updated_date, domain_id, data_changes FROM links ORDER BY %s %s %s";
    private static final String SELECT_BY_MANY_LINK_ID_QUERY = "SELECT id, link, page_updated_date, domain_id, data_changes FROM links WHERE id IN (%s)";
    private static final String UPDATE_DATA_CHANGES_QUERY = "UPDATE links SET data_changes=?, page_updated_date=? where id=?";

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void add(Link linkData) {
        checkEntity(linkData);
        template.update(
                INSERT_QUERY,
                linkData.getLink(),
                linkData.getDomain().getId(),
                linkData.getPageUpdatedDate(),
                CONVERTOR.convertToDatabaseColumn(linkData.getDataChanges())
        );
    }

    @Override
    public void remove(long id) {
        template.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void remove(String link) {
        template.update(DELETE_BY_LINK_QUERY, link);
    }

    @Override
    public void updateUpdatedDateLink(long linkId) {
        template.update(UPDATE_LATEST_SCHEDULER_DATE_QUERY, linkId);
    }

    @Override
    public void updateDataChangesLink(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId) {
        template.update(
                UPDATE_DATA_CHANGES_QUERY,
                CONVERTOR.convertToDatabaseColumn(dataChanges),
                updatedDate,
                linkId
        );
    }

    public Link getByLink(String link) {
        return template.queryForObject(SELECT_BY_LINK_QUERY, rowMapper, link);
    }

    @Override
    public List<Link> findAll(String nameField, boolean orderASC, Integer limit) {
        return template.query(
                String.format(
                        SELECT_ALL_QUERY,
                        nameField.isEmpty() ? "id" : nameField,
                        orderASC ? "" : "DESC",
                        limit == null ? "" : String.format("LIMIT %d", limit)
                ),
                rowMapper
        );
    }

    @Override
    public List<Link> getByLinkIds(List<Long> arrChatLink) {
        String inSql = String.join(",", Collections.nCopies(arrChatLink.size(), "?"));
        return template.query(
                String.format(SELECT_BY_MANY_LINK_ID_QUERY, inSql),
                rowMapper,
                arrChatLink.toArray()
        );
    }
}
