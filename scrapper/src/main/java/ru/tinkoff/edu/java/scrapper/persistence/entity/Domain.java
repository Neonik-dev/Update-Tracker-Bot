package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "domains")
public class Domain {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name="domain_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="domain_id_seq")
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name="created_date", nullable = false, updatable = false)
    private LocalDate createdDate;
    @OneToMany(mappedBy = "domain", fetch = FetchType.LAZY)
    private Set<Link> links = new HashSet<>();

    public Domain(Long id, String name, LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }
}
