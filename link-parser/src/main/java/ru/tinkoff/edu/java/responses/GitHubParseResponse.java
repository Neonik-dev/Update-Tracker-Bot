package ru.tinkoff.edu.java.responses;

public record GitHubParseResponse(String user, String repo) implements BaseParseResponse {
}
