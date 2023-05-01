package ru.tinkoff.edu.java.bot.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;
import ru.tinkoff.edu.java.bot.logic.wrapper.TgBot;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SendBotUpdatedLink {
    private final TgBot tgBot;

    public void sendUpdate(LinkUpdateRequest request) {
        String text = request.url().toString() + "\n\n" + request.description();
        Arrays.stream(request.tgChatIds()).forEach(
                (id) -> tgBot.sendMessage(SendSimpleMessage.create(id, text))
        );
    }
}
