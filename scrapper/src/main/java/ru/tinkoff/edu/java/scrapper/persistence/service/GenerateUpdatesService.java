package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.GeneralParseLink;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class GenerateUpdatesService {
    private final LinkService linkService;
    private final ChatLinkService chatLinkService;
    private final SitesMap sitesMap;
    GenerateUpdatesService(LinkService linkService, SitesMap sitesMap, @Lazy ChatLinkService chatLinkService) {
        this.linkService = linkService;
        this.chatLinkService = chatLinkService;
        this.sitesMap = sitesMap;
    }
    public Map<String, String> getSiteDataChanges(String link) {
        BaseParseResponse parseResponse = new GeneralParseLink().main(link); // Парсинг ссылки на необходимые данные
        return sitesMap.getClient(URI.create(link).getHost()).getUpdates(parseResponse); // Получение значений отслеживаемых полей
    }

    public Optional<LinkUpdateRequest> getUpdates() {
        Optional<LinkData> linkData = linkService.getOldestUpdateLink();
        if (linkData.isEmpty())
            return Optional.empty();
        LinkData clearLinkData = linkData.get();

        Map<String, String> responseDataChanges = getSiteDataChanges(clearLinkData.getLink());
        if (clearLinkData.getPageUpdateDate().toString().equals(responseDataChanges.get("updated_date"))) {
            return Optional.empty();
        }

        Map<String, String> dataChanges = clearLinkData.getDataChanges(); // Данные из бд, которые отслеживаем у ссылки
        StringBuilder text = new StringBuilder("Есть обновление"); // генерирую сообщение пользователю
        for (String key : responseDataChanges.keySet()) {
            if (dataChanges.get(key) != null && !Objects.equals(dataChanges.get(key), responseDataChanges.get(key))) {
//                text.append("\n~~").append(key).append(": ").append(dataChanges.get(key)).append("~~ -> ")
//                .append(key).append(": ").append(responseDataChanges.get(key));    // как лучше сделать?
                text.append(String.format("\n~~%s: %s~~ -> %s: %s",
                                          key,
                                          dataChanges.get(key),
                                          key,
                                          responseDataChanges.get(key)));
            }
        }
        linkService.updateDataChanges(responseDataChanges, clearLinkData.getId()); // записываю обновленные данные в бд

        return Optional.of(new LinkUpdateRequest(clearLinkData.getId(),
                                     URI.create(clearLinkData.getLink()),
                                     text.toString(),
                                     chatLinkService.getAllChat(clearLinkData.getId())));
    }
}
