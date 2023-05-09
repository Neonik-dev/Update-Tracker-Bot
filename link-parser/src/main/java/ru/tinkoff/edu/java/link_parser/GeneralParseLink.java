package ru.tinkoff.edu.java.link_parser;

import ru.tinkoff.edu.java.link_parser.parsers.ParseChain;
import ru.tinkoff.edu.java.link_parser.parsers.Parser;
import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;

import java.util.Optional;

public class GeneralParseLink {
    private static final Parser PARSER = ParseChain.chain();

    public BaseParseResponse start(String link) {
        // Chain of responsibility
        return PARSER.parseUrl(Optional.ofNullable(link));
    }
}
