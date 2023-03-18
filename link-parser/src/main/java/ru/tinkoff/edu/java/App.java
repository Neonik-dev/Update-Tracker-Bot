package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parsers.BaseParser;
import ru.tinkoff.edu.java.parsers.GitHubParser;
import ru.tinkoff.edu.java.parsers.StackOverflowParser;
import ru.tinkoff.edu.java.responses.BaseResponse;


public class App
{
    public BaseResponse main(String link)
    {
        // Chain of responsibility
        BaseParser githubParser = new GitHubParser();
        BaseParser stackOverflowParser = new StackOverflowParser();
        githubParser.Successor = stackOverflowParser;

        return githubParser.parseUrl(link);
    }

    // used for testing
//    public static void main(String[] args)
//    {
//        String link = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
//        // Chain of responsibility
//        BaseParser githubParser = new GitHubParser();
//        BaseParser stackOverflowParser = new StackOverflowParser();
//        githubParser.Successor = stackOverflowParser;
//
//        BaseResponse response = githubParser.parseUrl(link);
//        if (response instanceof GitHubResponse githubResponse) {
//            System.out.println(githubResponse); // GitHubResponse[user=sanyarnd, repo=tinkoff-java-course-2022]
//        } else if (response instanceof StackOverflowResponse stackOverflowResponse) {
//            System.out.println(stackOverflowResponse); // StackOverflowResponse[questionId=1642028]
//        } else {
//            System.out.println("null");
//        }
//    }
}
