package ru.tinkoff.edu.java.bot.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record RabbitPropertyConfig(
        ExchangeProperty exchange,
        @NotNull QueueProperty queue,
        RoutingKeyProperty routingKey,
        @NotNull String host,
        @NotNull Integer port,
        @NotNull String username,
        @NotNull String password
) {
}
