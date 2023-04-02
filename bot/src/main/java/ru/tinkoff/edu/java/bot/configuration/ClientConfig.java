package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ScrapperConfiguration scrapperConfig;
    @Bean("ScrapperClient")
    public ScrapperClient getScrapperClient() {
        return new ScrapperClient(scrapperConfig);
    }
}
