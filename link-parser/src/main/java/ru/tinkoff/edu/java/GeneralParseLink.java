package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parsers.ParseChain;
import ru.tinkoff.edu.java.parsers.Parser;
import ru.tinkoff.edu.java.responses.BaseParseResponse;

import java.util.Optional;


public class GeneralParseLink
{
    private static final Parser parser = ParseChain.chain();
    public BaseParseResponse main(String link)
    {
        // Chain of responsibility
        return parser.parseUrl(Optional.ofNullable(link));
    }
}
