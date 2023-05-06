package ru.tinkoff.edu.java.parser_link;

import ru.tinkoff.edu.java.parser_link.parsers.ParseChain;
import ru.tinkoff.edu.java.parser_link.parsers.Parser;
import ru.tinkoff.edu.java.parser_link.responses.BaseParseResponse;

import java.util.Optional;


public class GeneralParseLink
{
    private static final Parser parser = ParseChain.chain();
    public BaseParseResponse start(String link)
    {
        // Chain of responsibility
        return parser.parseUrl(Optional.ofNullable(link));
    }
}
