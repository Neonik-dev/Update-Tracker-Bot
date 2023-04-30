package ru.tinkoff.edu.java.scrapper.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record RoutingKeyProperty(
        @NotNull String botUpdateRoutingKey
) {
}
