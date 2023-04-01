package ru.tinkoff.edu.java.scrapper.controllers;


import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        HashSet<URI> links = LinkManager.getLinks();
        if (links.isEmpty()) {
            return new ListLinksResponse(null, 0);
        }

        List<LinkResponse> listLinks = new ArrayList<>();
        for (URI url : links) {
            listLinks.add(new LinkResponse(0L, url));
        }
        return new ListLinksResponse(listLinks, listLinks.size());
    }

    @PostMapping
    public LinkResponse postLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody AddLinkRequest request
    ) {
        LinkManager.add(request.url());
        return new LinkResponse(tgChatId, request.url());
    }

    @DeleteMapping
    public LinkResponse deleteLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody RemoveLinkRequest request
    ) {
        LinkManager.remove(request.url());
        return new LinkResponse(tgChatId, request.url());
    }
}
