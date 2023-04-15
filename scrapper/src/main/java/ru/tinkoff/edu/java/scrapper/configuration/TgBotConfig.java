package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "tg.bot")
@Configuration("tgBot")
public class TgBotConfig {
    @Value("${baseUrl:http://localhost:8080}")
    private String baseUrl;
}
