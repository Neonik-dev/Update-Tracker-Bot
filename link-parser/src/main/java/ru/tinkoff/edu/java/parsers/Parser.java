package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseParseResponse;

import java.util.Optional;

public interface Parser {
    BaseParseResponse parseUrl(Optional<String> url);
}
