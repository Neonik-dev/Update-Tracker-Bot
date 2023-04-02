package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "client.scrapper")
@Configuration("scrapper")
public class ScrapperConfiguration {
    @Value("${baseUrl:http://localhost:8080}")
    private static String baseUrl;

    public static String getBaseUrl() {
        return baseUrl;
    }
}
