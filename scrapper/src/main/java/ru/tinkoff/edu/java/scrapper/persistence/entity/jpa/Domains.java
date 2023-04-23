package ru.tinkoff.edu.java.scrapper.persistence.entity.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "domains")
public class Domains {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name="domain_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="domain_id_seq")
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name="created_date", nullable = false, updatable = false)
    private LocalDate createdDate;
    @OneToMany(mappedBy = "domainId", fetch = FetchType.LAZY)
    private Set<Links> links = new HashSet<>();
}
