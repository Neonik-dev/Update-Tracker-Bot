package ru.tinkoff.edu.java.link_parser.responses;

public sealed interface BaseParseResponse permits GitHubParseResponse, StackOverflowParseResponse {
}
