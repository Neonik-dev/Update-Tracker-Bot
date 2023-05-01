package ru.tinkoff.edu.java.bot.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record QueueProperty(
        @NotNull String updateQueue,
        @NotNull String updateDLQ
) {
}
