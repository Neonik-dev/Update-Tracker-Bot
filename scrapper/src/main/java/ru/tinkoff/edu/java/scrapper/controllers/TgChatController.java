package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat/{id}")
public class TgChatController {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void postTgChat(@PathVariable("id") Long id) {
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteTgChat(@PathVariable("id") Long id) {
    }
}
