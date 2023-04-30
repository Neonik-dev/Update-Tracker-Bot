package ru.tinkoff.edu.java.bot.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record RabbitPropertyConfig(
        @NotNull ExchangeProperty exchange,
        @NotNull QueueProperty queue,
        @NotNull RoutingKeyProperty routingKey,
        @NotNull String host,
        @NotNull String username,
        @NotNull String password
) {
}
