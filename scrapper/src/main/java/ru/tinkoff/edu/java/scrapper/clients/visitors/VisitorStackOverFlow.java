package ru.tinkoff.edu.java.scrapper.clients.visitors;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.StackOverFlowQuestionsResponse;

public class VisitorStackOverFlow {
    private static final String baseUrl = "https://api.stackexchange.com";
    private final WebClient.Builder builder;

    public VisitorStackOverFlow() {
        this(baseUrl);
    }

    public VisitorStackOverFlow(String baseUrl) {
        builder = WebClient.builder().baseUrl(baseUrl);
    }

    public BaseResponse getInformation(long id) {
        return builder.build().get().uri("questions/{id}?site=stackoverflow", id).retrieve().bodyToMono(StackOverFlowQuestionsResponse.class).block().questions()[0];
    }

//    public static void main(String[] args) {
//        WebClient.Builder builder = WebClient.builder().baseUrl(baseUrl);
//        long id = 2804041;
//
//        StackOverFlowQuestionsResponse parse = builder.build().get().uri("questions/{id}?site=stackoverflow", id).retrieve().bodyToMono(StackOverFlowQuestionsResponse.class).block();
//        System.out.println("-----------------------------------");
//        System.out.println(parse.questions()[0]);
//        System.out.println("-----------------------------------");
//    }
}
