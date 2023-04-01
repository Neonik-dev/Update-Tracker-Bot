package ru.tinkoff.edu.java.bot.clients.dto;

import java.net.URI;

public record LinkResponse(
        Long id,
        URI url
) {
}
