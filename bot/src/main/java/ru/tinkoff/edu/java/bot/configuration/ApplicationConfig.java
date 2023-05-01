package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.configuration.rabbit.RabbitPropertyConfig;

@EnableRabbit
@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        @NotNull String test,
        @NotNull String token,
        @NotNull RabbitPropertyConfig rabbit
) {
}
