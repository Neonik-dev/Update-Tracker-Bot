package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "db.postgresql")
public record PostgresqlConfig(String url, String username, String password) {
}