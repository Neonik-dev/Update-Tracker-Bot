package ru.tinkoff.edu.java.scrapper.clients.clients.notifier;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements SenderUpdatedLinks{
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.routingKey.botUpdateRoutingKey}")
    private String BOT_ROUTING_KEY;
    @Value("${app.rabbit.exchange.botUpdateExchange}")
    private String BOT_EXCHANGE;

    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        rabbitTemplate.convertAndSend(BOT_EXCHANGE, BOT_ROUTING_KEY, linkUpdateRequest);
    }
}
