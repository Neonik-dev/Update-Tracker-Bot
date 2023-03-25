package ru.tinkoff.edu.java.scrapper.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.requests.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.requests.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinkResponse;


@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ResponseEntity<ListLinkResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return ResponseEntity.ok().body(new ListLinkResponse(null, 0));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> postLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody AddLinkRequest request
    ) {
        return ResponseEntity.ok().body(new LinkResponse(tgChatId, request.url()));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody RemoveLinkRequest request
    ) {
        return ResponseEntity.ok().body(new LinkResponse(tgChatId, request.url()));
    }
}
