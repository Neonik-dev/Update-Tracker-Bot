package ru.tinkoff.edu.java.bot.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;

@Component
public class BotConsumer {
    @RabbitListener(queues = "${app.rabbit.queue.updateQueue}")
    public void receive(LinkUpdateRequest linkUpdateRequest) {
//        System.out.println("Message " + linkUpdateRequest.description());
    }
}