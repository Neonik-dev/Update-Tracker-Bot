package ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class ChatData {
    @Id
    private Long id;
    private OffsetDateTime createdDate;
    private LocalDate lastCallDate;
}
