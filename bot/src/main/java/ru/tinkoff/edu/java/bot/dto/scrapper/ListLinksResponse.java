package ru.tinkoff.edu.java.bot.dto.scrapper;

import java.util.List;

public record ListLinksResponse(
        List<LinkResponse> links,
        Integer size
) {
}
