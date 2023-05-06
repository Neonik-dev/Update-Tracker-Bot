package ru.tinkoff.edu.java.link_parser.parsers;

import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
import ru.tinkoff.edu.java.link_parser.responses.StackOverflowParseResponse;

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
