package ru.tinkoff.edu.java.scrapper.clients.clients.notifier;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.configuration.rabbit.RabbitPropertyConfig;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements SenderUpdatedLinks {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitPropertyConfig rabbitPropertyConfig;

    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        rabbitTemplate.convertAndSend(
                rabbitPropertyConfig.exchange().botUpdateExchange(),
                rabbitPropertyConfig.routingKey().botUpdateRoutingKey(),
                linkUpdateRequest
        );
    }
}
