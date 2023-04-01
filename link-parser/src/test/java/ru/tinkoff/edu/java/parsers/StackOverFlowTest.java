package ru.tinkoff.edu.java.parsers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.responses.StackOverflowResponse;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class StackOverFlowTest {
    static Parser parser;
    @BeforeAll
    static void beforeAll() {
        parser = ParseChain.chain();
    }

    private StackOverflowResponse useParser(String link) {
        return (StackOverflowResponse) parser.parseUrl(link);
    }
    @Test
    void StackParser_LinkWithoutQuestion_Null() {
        String link = "https://stackoverflow.com/questions//what-is-the-operator-in-c";

        StackOverflowResponse response = useParser(link);

        assertNull(response);
    }

    @Test
    void StackParser_GoodLink_OK() {
        String link = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";

        StackOverflowResponse response = useParser(link);

        assertEquals(response.questionId(), "1642028");
    }

}
