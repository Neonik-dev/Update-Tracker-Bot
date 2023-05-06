package ru.tinkoff.edu.java.link_parser.parsers;

import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;

import java.util.Optional;

public interface Parser {
    BaseParseResponse parseUrl(Optional<String> url);
}
