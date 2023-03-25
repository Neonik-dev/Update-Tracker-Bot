package ru.tinkoff.edu.java.scrapper.responses;

import java.net.URI;

public record LinkResponse(
        Long id,
        URI url
) {
}
