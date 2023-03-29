package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix="client.stackoverflow")
@Configuration("stackoverflow")
public class StackOverFlowConfig {
    @Value("${baseUrl:https://api.stackexchange.com}")
    private String baseUrl;
}
