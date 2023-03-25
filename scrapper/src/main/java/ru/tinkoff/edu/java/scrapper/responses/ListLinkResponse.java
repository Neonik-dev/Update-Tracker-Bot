package ru.tinkoff.edu.java.scrapper.responses;

public record ListLinkResponse(
        LinkResponse links,
        Integer size
) {
}
