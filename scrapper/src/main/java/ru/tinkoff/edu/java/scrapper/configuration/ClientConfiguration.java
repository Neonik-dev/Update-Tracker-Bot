package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.clients.TgBotClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.StackOverFlowClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.GitHubClient;

@Configuration
@RequiredArgsConstructor
//@ContextConfiguration(classes = {GitHubConfig.class, StackOverFlowConfig.class, TgBotConfig.class})
public class ClientConfiguration {
    private final GitHubConfig gitHubConfig;
    private final StackOverFlowConfig stackOverFlowConfig;
    private final TgBotConfig tgBotConfig;
    @Bean("gitHubClient")
    public GitHubClient getGitHubClient() {
        return new GitHubClient(gitHubConfig);
    }

    @Bean("stackOverFlowClient")
    public StackOverFlowClient getStackOverFlowClient() {
        return new StackOverFlowClient(stackOverFlowConfig);
    }

    @Bean("telegramBotClient")
    public TgBotClient getTgBotClient() {
        return new TgBotClient(tgBotConfig);
    }

    @Bean("schedulerIntervalMs")
    public long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }
}
