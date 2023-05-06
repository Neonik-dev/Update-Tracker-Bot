package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.responses.StackOverflowParseResponse;

import java.util.Optional;

public final class StackOverflowParser extends BaseParser {
    private static final String URL_TEMPLATE = "https://stackoverflow\\.com/questions/(?:\\d+/.*|\\d+)";
    @Override
    public BaseParseResponse parseUrl(Optional<String> url) {
        if (url.isPresent() && url.get().matches(URL_TEMPLATE)) {
            String[] args = url.get().split("/");
            String questionId = args[4];
            return new StackOverflowParseResponse(questionId);
        }
        return nextParse(url);
    }
}
