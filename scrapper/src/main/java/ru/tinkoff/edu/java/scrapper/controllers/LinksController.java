package ru.tinkoff.edu.java.scrapper.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.requests.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.requests.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinkResponse;


@RestController
@RequestMapping("scrapper/links")
@ResponseStatus(HttpStatus.OK)
public class LinksController {
    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return new ListLinkResponse(null, 0);
    }

    @PostMapping
    public LinkResponse postLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody AddLinkRequest request
    ) {
        return new LinkResponse(tgChatId, request.url());
    }

    @DeleteMapping
    public LinkResponse deleteLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody RemoveLinkRequest request
    ) {
        return new LinkResponse(tgChatId, request.url());
    }
}
