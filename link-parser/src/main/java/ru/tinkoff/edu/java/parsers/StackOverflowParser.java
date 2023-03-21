package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;
import ru.tinkoff.edu.java.responses.StackOverflowResponse;

public final class StackOverflowParser extends BaseParser {
    private static final String RE_URL = "https://stackoverflow\\.com/questions/(?:\\d+/.*|\\d+)";
    @Override
    public BaseResponse parseUrl(String url) {
        if (url.matches(RE_URL)) {
            String[] args = url.split("/");
            String questionId = args[4];
            return new StackOverflowResponse(questionId);
        }
        return nextParse(url);
    }
}
