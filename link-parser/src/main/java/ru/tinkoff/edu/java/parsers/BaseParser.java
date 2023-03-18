package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;


public sealed abstract class BaseParser permits GitHubParser, StackOverflowParser {
    public BaseParser Successor;

    public abstract BaseResponse parseUrl(String url);

    protected BaseResponse nextParse(String url) {
        return Successor != null ? Successor.parseUrl(url) : null;
    }
}
