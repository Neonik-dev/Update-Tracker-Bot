package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;
import ru.tinkoff.edu.java.responses.StackOverflowResponse;

public final class StackOverflowParser extends BaseParser {
    @Override
    public BaseResponse parseUrl(String url) {
        if ((url + '/').matches("https://stackoverflow\\.com/questions/\\d+/.*")) {
            String[] args = url.split("/");
            String questionId = args[4];
            return new StackOverflowResponse(questionId);
        }
        return nextParse(url);
    }
}
