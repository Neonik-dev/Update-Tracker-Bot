package ru.tinkoff.edu.java.scrapper.clients.visitors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubResponse;

public class VisitorGitHub implements LinkVisitor{
    private final WebClient.Builder builder;
    @Value("${github.token}")
    private String token;

    public VisitorGitHub() {

        this("https://api.github.com/repos/Neonik228/Tinkoff_project");
    }

    public VisitorGitHub(String baseUrl) {
        builder = WebClient.builder()
                .defaultHeader("Authorization", token)
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .defaultHeader("Accept", "application/vnd.github+json")
                .baseUrl(baseUrl);
    }

    public GitHubResponse getInformation() {
        return builder.build().get().retrieve().bodyToMono(GitHubResponse.class).block();
    }

//    public static void main(String[] args) {
//        String url = "https://api.github.com/repos/gleb/compas-analysis";
//
//        WebClient.Builder builder = WebClient.builder()
//                .defaultHeader("Authorization", token)
//                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
//                .defaultHeader("Accept", "application/vnd.github+json")
//                .baseUrl(url);
//
//
//        GitHubResponse parse = builder.build().get().retrieve().bodyToMono(GitHubResponse.class).block();
//        System.out.println("-----------------------------------");
//        System.out.println(parse);
//        System.out.println("-----------------------------------");
//    }
}
