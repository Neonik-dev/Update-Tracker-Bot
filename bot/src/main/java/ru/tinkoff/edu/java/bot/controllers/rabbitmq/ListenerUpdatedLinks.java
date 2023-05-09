package ru.tinkoff.edu.java.bot.controllers.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.logic.SendBotUpdatedLink;

@Component
@RabbitListener(queues = "${app.rabbit.queue.updateQueue}")
@RequiredArgsConstructor
public class ListenerUpdatedLinks {
    private final SendBotUpdatedLink sendBotUpdatedLink;

    @RabbitHandler
    public void receive(LinkUpdateRequest linkUpdateRequest) {
        sendBotUpdatedLink.sendUpdate(linkUpdateRequest);
    }
}
