package ru.tinkoff.edu.java.scrapper.scheduller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.GeneralParseLink;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.clients.TgBotClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final TgBotClient botClient;
    private final LinkService linkService;
    private final ChatLinkService chatLinkService;
    private final SitesMap sitesMap;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        Optional<LinkData> linkData = linkService.getOldestUpdateLink();
        if (linkData.isEmpty())
            return;
        LinkData clearLinkData = linkData.get();
        BaseParseResponse parseResponse = new GeneralParseLink().main(clearLinkData.getLink());
//        Map<String, String> response = sitesMap.getClient(URI.create(clearLinkData.getLink()).getHost()).getUpdates(parseResponse).getMap();
//        Map<String, String> dataChanges = clearLinkData.getDataChanges();
//        if (!clearLinkData.getPageUpdateDate().toString().equals(response.get("updated_date"))) { // сделать это безопасно
//            botClient.postUpdates(new LinkUpdateRequest(clearLinkData.getId(), URI.create(clearLinkData.getLink()), "Есть обновление", chatLinkService.getAllChat(clearLinkData.getId())));
//        }
        log.info("Updating the link on a schedule id = " + clearLinkData.getId());
    }
}
