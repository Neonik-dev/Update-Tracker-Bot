package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.logic.commands.ListCommand;
import ru.tinkoff.edu.java.bot.logic.commands.StartCommand;
import ru.tinkoff.edu.java.bot.logic.commands.TrackCommand;
import ru.tinkoff.edu.java.bot.logic.commands.UntrackCommand;

@Configuration
@RequiredArgsConstructor
public class CommandConfig {
    private final ScrapperClient scrapperClient;

    @Bean("startCommand")
    public StartCommand getStartCommand() {
        return new StartCommand(scrapperClient);
    }

    @Bean("trackCommand")
    public TrackCommand getTrackCommand() {
        return new TrackCommand(scrapperClient);
    }

    @Bean("untrackCommand")
    public UntrackCommand getUntrackCommand() {
        return new UntrackCommand(scrapperClient);
    }

    @Bean("listCommand")
    public ListCommand getListCommand() {
        return new ListCommand(scrapperClient);
    }
}