package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "client.github")
@Configuration("github")
public class GitHubConfig {
    @Value("${baseUrl:https://api.github.com}")
    private String baseUrl;
}
