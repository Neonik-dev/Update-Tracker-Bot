package ru.tinkoff.edu.java.scrapper.dto;

import java.net.URI;

public record LinkResponse(
        Long id,
        URI url
) {
}
