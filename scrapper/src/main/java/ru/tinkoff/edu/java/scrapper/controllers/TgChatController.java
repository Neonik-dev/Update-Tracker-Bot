package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class TgChatController {
    private final ChatService chatService;
    @PostMapping
    public void postTgChat(@PathVariable("id") Long id) throws DuplicateUniqueFieldException, BadEntityException {
        chatService.register(id);
    }

    @DeleteMapping
    public void deleteTgChat(@PathVariable("id") Long id) {
        chatService.unregister(id);
    }
}
