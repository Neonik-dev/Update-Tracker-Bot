package ru.tinkoff.edu.java.bot.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.logic.SendBotUpdatedLink;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {
    private final SendBotUpdatedLink sendBotUpdatedLink;

    @PostMapping
    public void updateLink(@RequestBody LinkUpdateRequest request) {
        sendBotUpdatedLink.sendUpdate(request);
    }
}
