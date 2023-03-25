package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.visitors.VisitorStackOverFlow;
import ru.tinkoff.edu.java.scrapper.clients.visitors.VisitorGitHub;

@Configuration
public class ClientConfiguration {
    @Bean
    public VisitorGitHub getWebClientGitHub() {
        return new VisitorGitHub();
    }

    @Bean
    public VisitorStackOverFlow getVisitorStackOverFlow() {
        return new VisitorStackOverFlow();
    }
}
