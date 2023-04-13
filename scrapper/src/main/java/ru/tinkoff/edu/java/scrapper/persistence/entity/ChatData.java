package ru.tinkoff.edu.java.scrapper.persistence.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class ChatData {
    @Id
    private Long id;
    private OffsetDateTime createdDate;
    private LocalDate lastCallDate;
}
