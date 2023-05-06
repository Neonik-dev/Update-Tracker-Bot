package ru.tinkoff.edu.java.parser_link.parsers;

import ru.tinkoff.edu.java.parser_link.responses.BaseParseResponse;

import java.util.Optional;

public interface Parser {
    BaseParseResponse parseUrl(Optional<String> url);
}
