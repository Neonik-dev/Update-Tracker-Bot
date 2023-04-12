package ru.tinkoff.edu.java.scrapper.persistence.entity;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.persistence.entity.domain.GitHubJson;
import ru.tinkoff.edu.java.scrapper.persistence.entity.domain.MyConvertor;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Table(name = "links")
@Entity
public class LinkData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "link", nullable = false, unique = true)
    private String link;
    @Column(name = "updated_date", nullable = false)
    private OffsetDateTime updateDate;
    @Column(name = "user_check_date", nullable = false)
    private OffsetDateTime userCheckDate;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "domain_id", referencedColumnName = "id", nullable = false)
//    private DomainData domainId;
    @Column(name="domain_id", nullable = false)
    private long domainId;
    @Column(name="data_changes", nullable = false)
    @Convert(converter = MyConvertor.class)
    private Map<String, String> dataChanges;
}
