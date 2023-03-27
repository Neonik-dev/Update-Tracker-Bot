package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat/{id}")
public class TgChatController {
    @PostMapping
    public void postTgChat(@PathVariable("id") Long id) {
    }

    @DeleteMapping
    public void deleteTgChat(@PathVariable("id") Long id) {
    }
}
