package ru.tinkoff.edu.java.parser_link.responses;

public record GitHubParseResponse(String user, String repo) implements BaseParseResponse {
}
