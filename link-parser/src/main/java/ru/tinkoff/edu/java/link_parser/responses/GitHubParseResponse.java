package ru.tinkoff.edu.java.link_parser.responses;

public record GitHubParseResponse(String user, String repo) implements BaseParseResponse {
}
