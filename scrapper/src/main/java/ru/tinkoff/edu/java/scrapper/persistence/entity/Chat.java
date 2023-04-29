package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "created_date", nullable = false, updatable = false)
    private OffsetDateTime createdDate;
    @Column(name = "last_call_date", nullable = false)
    private LocalDate lastCallDate;
}
