package ru.tinkoff.edu.java.scrapper.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record QueueProperty(
        @NotNull String botUpdateQueue
) {
}
