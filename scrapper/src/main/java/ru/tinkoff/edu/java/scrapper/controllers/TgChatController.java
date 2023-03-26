package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("scrapper/tg-chat/{id}")
@ResponseStatus(HttpStatus.OK)
public class TgChatController {
    @PostMapping
    public void postTgChat(@PathVariable("id") Long id) {
    }

    @DeleteMapping
    public void deleteTgChat(@PathVariable("id") Long id) {
    }
}
