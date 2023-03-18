package ru.tinkoff.edu.java.responses;

public record GitHubResponse(String user, String repo) implements BaseResponse {
}
