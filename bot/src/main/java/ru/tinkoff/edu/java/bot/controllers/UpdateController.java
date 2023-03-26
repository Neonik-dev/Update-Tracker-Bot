package ru.tinkoff.edu.java.bot.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.requests.LinkUpdateRequest;

@RestController
@RequestMapping("bot/updates")
public class UpdateController {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateLink(@RequestBody LinkUpdateRequest request) {
    }

}
