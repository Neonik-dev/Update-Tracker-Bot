package ru.tinkoff.edu.java.link_parser.parsers;

import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
import ru.tinkoff.edu.java.link_parser.responses.GitHubParseResponse;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GitHubParser extends BaseParser {
    private static final Pattern URL_TEMPLATE = Pattern.compile("^https://github\\.com/([^/]+?)/([^/]+).*");

    @Override
    public BaseParseResponse parseUrl(Optional<String> url) {
        Matcher matcher = URL_TEMPLATE.matcher(url.orElse(""));
        int i = 1;
        if (matcher.find()) {
            String user = matcher.group(1);
            String repo = matcher.group(2);
            return new GitHubParseResponse(user, repo);
        }
        return nextParse(url);
    }
}
