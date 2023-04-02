package ru.tinkoff.edu.java.bot.configuration;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.logic.TgUpdaterLinkBot;
import ru.tinkoff.edu.java.bot.logic.utils.InputHandler;

@Configuration
@RequiredArgsConstructor
public class TgBotConfig {
    private TgUpdaterLinkBot tgBot;
    private final ApplicationConfig config;
    private final InputHandler inputHandler;
    @Bean("TgBot")
    public TgUpdaterLinkBot getTgBot() {
        tgBot = new TgUpdaterLinkBot(config, inputHandler);
        return tgBot;
    }

    @PreDestroy
    public void destroy() {
        tgBot.close();
    }
}