package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;
import ru.tinkoff.edu.java.responses.GitHubResponse;

public final class GitHubParser extends BaseParser {
    private static final String RE_URL = "https://github\\.com/[^/]+?/[^/].+";
    @Override
    public BaseResponse parseUrl(String url) {
        if (url.matches(RE_URL)) {
            String[] args = url.split("/");
            String user = args[3];
            String repo = args[4];
            return new GitHubResponse(user, repo);
        }
        return nextParse(url);
    }
}
