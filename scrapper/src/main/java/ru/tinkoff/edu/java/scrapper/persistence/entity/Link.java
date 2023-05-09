package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "links")
@Getter
@Setter
public class Link {
    @Id
    @SequenceGenerator(name = "link_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_id_seq")
    private Long id;
    @Column(name = "link", nullable = false, unique = true)
    private String link;
    @Column(name = "scheduler_updated_date", nullable = false)
    private OffsetDateTime schedulerUpdateDate;
    @Column(name = "page_updated_date", nullable = false)
    private OffsetDateTime pageUpdatedDate;
    @Column(name = "user_check_date", nullable = false)
    private OffsetDateTime userCheckDate;
    @Column(name = "data_changes", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> dataChanges;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Domain domain;
    @OneToMany(mappedBy = "link", fetch = FetchType.LAZY)
    private Set<ChatLink> chats = new HashSet<>();

    public Link(
            Long id,
            String link,
            OffsetDateTime schedulerUpdateDate,
            OffsetDateTime pageUpdatedDate,
            OffsetDateTime userCheckDate,
            Long domainId,
            Map<String, String> dataChanges
    ) {
        this.id = id;
        this.link = link;
        this.schedulerUpdateDate = schedulerUpdateDate;
        this.pageUpdatedDate = pageUpdatedDate;
        this.userCheckDate = userCheckDate;
        this.domain = new Domain(domainId, null, null);
        this.dataChanges = dataChanges;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public void setDomain(Long domainId) {
        this.domain = new Domain(domainId, null, null);
    }
}
