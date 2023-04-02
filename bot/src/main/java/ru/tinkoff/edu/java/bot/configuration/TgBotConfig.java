package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.logic.TgUpdaterLinkBot;

@Configuration
@RequiredArgsConstructor
public class TgBotConfig {
    private final ApplicationConfig config;
    @Bean("TgBot")
    public TgUpdaterLinkBot getTgBot() {
        return new TgUpdaterLinkBot(config);
    }
}