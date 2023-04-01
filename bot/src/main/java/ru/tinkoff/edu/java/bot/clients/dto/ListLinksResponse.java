package ru.tinkoff.edu.java.bot.clients.dto;

import java.util.List;

public record ListLinksResponse(
        List<LinkResponse> links,
        Integer size
) {
}
