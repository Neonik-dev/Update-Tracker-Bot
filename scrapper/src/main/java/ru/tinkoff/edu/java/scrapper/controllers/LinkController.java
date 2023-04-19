package ru.tinkoff.edu.java.scrapper.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;


@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;
    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return linkService.listAll(tgChatId);
    }

    @PostMapping
    public LinkResponse postLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody AddLinkRequest request
    ) throws DuplicateUniqueFieldException, EmptyResultException, ForeignKeyNotExistsException, BadEntityException {

        return linkService.add(tgChatId, request.url());
    }

    @DeleteMapping
    public LinkResponse deleteLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody RemoveLinkRequest request
    ) throws EmptyResultException {

        return linkService.remove(tgChatId, request.url());
    }
}
