package ru.tinkoff.edu.java.bot.configuration.rabbit;

import jakarta.validation.constraints.NotNull;

public record ExchangeProperty(
        @NotNull String updateDLX
) {
}