package ru.tinkoff.edu.java.scrapper.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record RabbitPropertyConfig(
        @NotNull ExchangeProperty exchange,
        @NotNull QueueProperty queue,
        @NotNull RoutingKeyProperty routingKey,
        @NotNull String host,
        @NotNull Integer port,
        @NotNull String username,
        @NotNull String password
) {
}
