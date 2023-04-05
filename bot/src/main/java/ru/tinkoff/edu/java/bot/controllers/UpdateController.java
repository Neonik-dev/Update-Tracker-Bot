package ru.tinkoff.edu.java.bot.controllers;


import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    @PostMapping
    public void updateLink(@RequestBody LinkUpdateRequest request) {
    }

}
