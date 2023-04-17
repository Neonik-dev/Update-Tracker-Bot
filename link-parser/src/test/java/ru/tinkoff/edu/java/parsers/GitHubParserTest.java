package ru.tinkoff.edu.java.parsers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.tinkoff.edu.java.GeneralParseLink;
import ru.tinkoff.edu.java.responses.GitHubParseResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class GitHubParserTest {
    @ParameterizedTest(name = "{index} - {0} is a bad link")
    @NullAndEmptySource
    void linkIsNullOrEmpty_Null(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().main(link);

        // then
        assertNull(response);
    }

    @ParameterizedTest(name = "{index} - {0} is a bad link")
    @ValueSource(strings = {
            "https://github.com//Tinkoff_project/pull/3",
            "https://githb.com/Name/Tinkoff_project/pull/3",
            "https://githb.com",
            "https://githb.com/Name",
            "https://githb.com/Name/",
            "https://githb.com/Name//"
    })
    void invalidLink_Null(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().main(link);

        // then
        assertNull(response);
    }

    @ParameterizedTest(name = "{index} - {0} is a valid link")
    @ValueSource(strings = {
            "https://github.com/Neonik228/Tinkoff_project/pull/3",
            "https://github.com/Neonik228/Tinkoff_project/",
            "https://github.com/Neonik228/Tinkoff_project",
    })
    void validLink_OK(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().main(link);

        // then
        assertAll(
                () -> assertEquals(response.repo(), "Tinkoff_project"),
                () -> assertEquals(response.user(), "Neonik228")
        );
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "https://github.com/Gram3r/Tinkoff-tourism",
            "https://github.com/Gram3r/Tinkoff-tourism/blob/main/src/main/java/tinkoff/tourism/controller/dto/RouteRequest.java",
    })
    void changeParserChain_OK(String link) {
        // given
        Parser parseChain = ParseChain.chain(new StackOverflowParser(), new GitHubParser());

        // when
        GitHubParseResponse response = (GitHubParseResponse) parseChain.parseUrl(Optional.ofNullable(link));

        // then
        assertAll(
                () -> assertEquals(response.user(), "Gram3r"),
                () -> assertEquals(response.repo(), "Tinkoff-tourism")
        );
    }
}
