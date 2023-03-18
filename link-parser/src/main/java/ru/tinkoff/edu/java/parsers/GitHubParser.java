package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;
import ru.tinkoff.edu.java.responses.GitHubResponse;

public final class GitHubParser extends BaseParser {
    @Override
    public BaseResponse parseUrl(String url) {
        if (url.matches("https://github\\.com/.*?/.+")) {
            String[] args = url.split("/");
            String user = args[3];
            String repo = args[4];
            return new GitHubResponse(user, repo);
        }
        return nextParse(url);
    }
}
