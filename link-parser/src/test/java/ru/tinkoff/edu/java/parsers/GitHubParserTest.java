package ru.tinkoff.edu.java.parsers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.responses.GitHubResponse;

import static junit.framework.Assert.*;

public class GitHubParserTest {
    static Parser parser;
    @BeforeAll
    static void beforeAll() {
        parser = ParseChain.chain();
    }

    private GitHubResponse useParser(String link) {
        return (GitHubResponse) parser.parseUrl(link);
    }
    @Test
    void GitHubParser_LinkWithoutRepo_Null() {
        String link = "https://github.com//Tinkoff_project/pull/3";

        GitHubResponse response = useParser(link);

        assertNull(response);
    }

    @Test
    void GitHubParser_GoodLink_OK() {
        String link = "https://github.com/Neonik228/Tinkoff_project/pull/3";

        GitHubResponse response = useParser(link);

        assertEquals(response.repo(), "Tinkoff_project");
        assertEquals(response.user(), "Neonik228");
    }

}
