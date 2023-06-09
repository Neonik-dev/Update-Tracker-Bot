package ru.tinkoff.edu.java.link_parser.parsers;

import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
import ru.tinkoff.edu.java.link_parser.responses.StackOverflowParseResponse;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StackOverflowParser extends BaseParser {
    private static final Pattern URL_TEMPLATE = Pattern.compile("^https://stackoverflow\\.com/questions/(\\d+)(?:/.*|$)");

    @Override
    public BaseParseResponse parseUrl(Optional<String> url) {
        Matcher matcher = URL_TEMPLATE.matcher(url.orElse(""));
        if (matcher.find()) {
            String questionId = matcher.group(1);
            return new StackOverflowParseResponse(questionId);
        }
        return nextParse(url);
    }
}
