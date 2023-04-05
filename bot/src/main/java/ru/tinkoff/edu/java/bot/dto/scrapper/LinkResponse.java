package ru.tinkoff.edu.java.bot.dto.scrapper;

import java.net.URI;

public record LinkResponse(
        Long id,
        URI url
) {
}
