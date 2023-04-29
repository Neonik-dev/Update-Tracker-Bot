package ru.tinkoff.edu.java.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "client.scrapper")
@Configuration("scrapper")
public class ScrapperConfiguration {
    @Value("${baseUrl:http://localhost:8081}")
    private String baseUrl;
}
