package ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Builder
@Data
public class DomainData {
    @Id
    private Long id;
    private String name;
    private LocalDate createdDate;
}
