package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;

import java.util.Optional;


public sealed abstract class BaseParser implements Parser permits GitHubParser, StackOverflowParser{
    private Optional<Parser> successor;

    public void setSuccessor(Parser successor) {
        this.successor = Optional.of(successor);
    }

    protected BaseResponse nextParse(String url) {
        return successor.isPresent() ? successor.get().parseUrl(url) : null;
    }
}
