package ru.tinkoff.edu.java.responses;

public sealed interface BaseResponse permits GitHubResponse, StackOverflowResponse {
}
