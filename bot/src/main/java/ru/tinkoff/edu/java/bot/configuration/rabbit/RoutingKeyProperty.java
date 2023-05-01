package ru.tinkoff.edu.java.bot.configuration.rabbit;

import org.jetbrains.annotations.NotNull;

public record RoutingKeyProperty(
        @NotNull String dlqUpdateRoutingKey
) {
}
