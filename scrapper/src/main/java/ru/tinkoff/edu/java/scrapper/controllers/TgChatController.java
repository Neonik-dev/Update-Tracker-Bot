package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.domain.ConvertorGitHub;
import ru.tinkoff.edu.java.scrapper.persistence.repository.DomainRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepositoryImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class TgChatController {
    private final LinkRepositoryImpl chatsRepositoryImpl;
    @PostMapping
    public void postTgChat(@PathVariable("id") Long id) {
//        UserManager.add(id);
        LinkData linkData = new LinkData();
        linkData.setLink("http://" + id);
        linkData.setDomainId(2L);
        linkData.setDataChanges(new HashMap<>() {{
            put("commits", "5");
            put("comments", "2");
        }});

        System.out.println(linkData.getDataChanges().values());
        chatsRepositoryImpl.add(linkData);
    }

    @DeleteMapping
    public void deleteTgChat(@PathVariable("id") Long id) {
//        UserManager.remove(id);
        chatsRepositoryImpl.remove(id);
    }
}
