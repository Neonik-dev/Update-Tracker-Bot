package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.rabbit.RabbitPropertyConfig;

import java.time.Duration;

@EnableRabbit
@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Scheduler scheduler,
        @NotNull AccessType databaseAccessType,
        @NotNull boolean useQueue,
        @NotNull RabbitPropertyConfig rabbit
) {
    public record Scheduler(Duration interval) {
    }

    public enum AccessType {
        JDBC, JOOQ, JPA
    }
}
