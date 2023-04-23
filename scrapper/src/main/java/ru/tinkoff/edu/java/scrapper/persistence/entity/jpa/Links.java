package ru.tinkoff.edu.java.scrapper.persistence.entity.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.ConverterDataChanges;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "links")
@Data
public class Links {
    @Id
    @SequenceGenerator(name="link_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="link_id_seq")
    private Long id;
    @Column(name = "link", nullable = false, unique = true)
    private String link;
    @Column(name = "scheduler_updated_date", nullable = false)
    private OffsetDateTime schedulerUpdateDate;
    @Column(name = "page_updated_date", nullable = false)
    private OffsetDateTime pageUpdatedDate;
    @Column(name = "user_check_date", nullable = false)
    private OffsetDateTime userCheckDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="domain_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Domains domainId;
    @Column(name="data_changes", nullable = false, columnDefinition = "jsonb")
    @Convert(converter = ConverterDataChanges.class)
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, String> dataChanges;
    @OneToMany(mappedBy = "link", fetch = FetchType.LAZY)
    private Set<ChatLink> chats = new HashSet<>();
}
