package ru.tinkoff.edu.java.scrapper.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.routingKey.botUpdateRoutingKey}")
    private String BOT_ROUTING_KEY;

    @Scheduled(fixedDelayString = "5000")
    public void send() {
        rabbitTemplate.convertAndSend(BOT_ROUTING_KEY, new LinkUpdateRequest(11L, null, "test rabbitmq", null));
    }
}
