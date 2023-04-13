package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepositoryImpl;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class TgChatController {
    private final LinkRepositoryImpl chatsRepositoryImpl;
    @PostMapping
    public void postTgChat(@PathVariable("id") Long id) {
        UserManager.add(id);
    }

    @DeleteMapping
    public void deleteTgChat(@PathVariable("id") Long id) {
//        UserManager.remove(id);
        chatsRepositoryImpl.remove(id);
    }
}
