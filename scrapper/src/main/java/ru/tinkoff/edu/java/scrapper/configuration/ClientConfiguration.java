package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.clients.notifier.BotClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.notifier.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.clients.clients.notifier.SenderUpdatedLinks;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.StackOverFlowClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.GitHubClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final GitHubConfig gitHubConfig;
    private final StackOverFlowConfig stackOverFlowConfig;
    private final TgBotConfig tgBotConfig;
    private final RabbitTemplate rabbitTemplate;

    @Bean("gitHubClient")
    public GitHubClient getGitHubClient() {
        return new GitHubClient(gitHubConfig);
    }

    @Bean("stackOverFlowClient")
    public StackOverFlowClient getStackOverFlowClient() {
        return new StackOverFlowClient(stackOverFlowConfig);
    }

    @Bean("telegramBotClient")
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
    public SenderUpdatedLinks getBotClient() {
        return new BotClient(tgBotConfig);
    }

    @Bean("senderUpdatedLinks")
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true", matchIfMissing = true)
    public SenderUpdatedLinks getScrapperQueueProducer() {
        return new ScrapperQueueProducer(rabbitTemplate);
    }

    @Bean("schedulerIntervalMs")
    public long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }
}
