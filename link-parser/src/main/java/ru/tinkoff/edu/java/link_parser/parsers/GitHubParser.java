package ru.tinkoff.edu.java.link_parser.parsers;

import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
import ru.tinkoff.edu.java.link_parser.responses.GitHubParseResponse;

import java.util.Optional;

public final class GitHubParser extends BaseParser {
    private static final String RE_URL = "^https://github\\.com/[^/]+?/[^/]+.*";
    @Override
    public BaseParseResponse parseUrl(Optional<String> url) {
        if (url.isPresent() && url.get().matches(RE_URL)) {
            String[] args = url.get().split("/");
            String user = args[3];
            String repo = args[4];
            return new GitHubParseResponse(user, repo);
        }
        return nextParse(url);
    }
}
