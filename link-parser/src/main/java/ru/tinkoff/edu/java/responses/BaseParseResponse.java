package ru.tinkoff.edu.java.responses;

public sealed interface BaseParseResponse permits GitHubParseResponse, StackOverflowParseResponse {
}
