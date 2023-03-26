package ru.tinkoff.edu.java.scrapper.clients.visitors;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubResponse;

public class VisitorGitHub {
    private static final String baseUrl = "https://api.github.com";
    private final WebClient.Builder builder;

    public VisitorGitHub() {
        this(baseUrl);
    }

    public VisitorGitHub(String baseUrl) {
        builder = WebClient.builder().baseUrl(baseUrl);
    }

    public BaseResponse getInformation(String user, String repo) {
        return builder.build().get().uri("repos/{user}/{repo}", user, repo).retrieve().bodyToMono(GitHubResponse.class).block();
    }

//    public static void main(String[] args) {
//        WebClient.Builder builder = WebClient.builder().baseUrl(baseUrl);
//        String user = "Neonik228";
//        String repo = "Tinkoff_project";
//
//
//        GitHubResponse parse = builder.build().get().uri("repos/{user}/{repo}", user, repo).retrieve().bodyToMono(GitHubResponse.class).block();
//        System.out.println("-----------------------------------");
//        System.out.println(parse);
//        System.out.println("-----------------------------------");
//    }
}
