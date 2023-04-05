package ru.tinkoff.edu.java.bot.dto.api;

import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
public record LinkUpdateRequest(
        Long id,
        URI url,
        String description,
        Long[] tgChatIds
) {
}
