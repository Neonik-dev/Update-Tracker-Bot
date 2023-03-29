package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.scheduller.Scheduler;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @NotNull Scheduler scheduler) {
}