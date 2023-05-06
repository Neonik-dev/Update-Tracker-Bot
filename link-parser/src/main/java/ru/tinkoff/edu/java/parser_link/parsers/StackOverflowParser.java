package ru.tinkoff.edu.java.parser_link.parsers;

import ru.tinkoff.edu.java.parser_link.responses.BaseParseResponse;
import ru.tinkoff.edu.java.parser_link.responses.StackOverflowParseResponse;

import java.util.Optional;

public final class StackOverflowParser extends BaseParser {
    private static final String RE_URL = "https://stackoverflow\\.com/questions/(?:\\d+/.*|\\d+)";
    @Override
    public BaseParseResponse parseUrl(Optional<String> url) {
        if (url.isPresent() && url.get().matches(RE_URL)) {
            String[] args = url.get().split("/");
            String questionId = args[4];
            return new StackOverflowParseResponse(questionId);
        }
        return nextParse(url);
    }
}
