package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(name = "Chats")
@Data
public class DomainData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name="created_date", nullable = false, updatable = false)
    private Date createdDate;
}
