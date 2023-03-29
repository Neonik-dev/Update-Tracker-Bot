package ru.tinkoff.edu.java.scrapper.dto;

public record ListLinkResponse(
        LinkResponse links,
        Integer size
) {
}
