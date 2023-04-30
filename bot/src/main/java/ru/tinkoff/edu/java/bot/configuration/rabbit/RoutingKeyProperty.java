package ru.tinkoff.edu.java.bot.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record RoutingKeyProperty(
        @NotNull String updateRoutingKey
) {
}
