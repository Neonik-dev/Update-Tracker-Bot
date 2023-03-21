package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parsers.BaseParser;
import ru.tinkoff.edu.java.parsers.ParseChain;
import ru.tinkoff.edu.java.responses.BaseResponse;


public class App
{
    public BaseResponse main(String link)
    {
        // Chain of responsibility
        BaseParser parser = ParseChain.chain();
        return parser.parseUrl(link);
    }

    // used for testing
//    public static void main(String[] args)
//    {
//        String link = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
//        BaseParser parser = ParseChain.chain();
//
//        BaseResponse response = parser.parseUrl(link);
//        if (response instanceof GitHubResponse githubResponse) {
//            System.out.println(githubResponse); // GitHubResponse[user=sanyarnd, repo=tinkoff-java-course-2022]
//        } else if (response instanceof StackOverflowResponse stackOverflowResponse) {
//            System.out.println(stackOverflowResponse); // StackOverflowResponse[questionId=1642028]
//        } else {
//            System.out.println("null");
//        }
//    }
}
