package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parsers.ParseChain;
import ru.tinkoff.edu.java.parsers.Parser;
import ru.tinkoff.edu.java.responses.BaseResponse;

import java.util.Optional;


public class App
{
    public BaseResponse main(String link)
    {
        // Chain of responsibility
        Parser parser = ParseChain.chain();
        return parser.parseUrl(Optional.ofNullable(link));
    }
}
