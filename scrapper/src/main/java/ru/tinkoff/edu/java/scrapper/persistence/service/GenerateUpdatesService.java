package ru.tinkoff.edu.java.scrapper.persistence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.GeneralParseLink;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.BaseSiteClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerateUpdatesService {
    private final LinkService linkService;
    private final ChatLinkService chatLinkService;
    private final SitesMap sitesMap;

    public Optional<LinkUpdateRequest> getUpdates() {
        Link clearLinkData;
        try {
            clearLinkData = getOldestsLink();
        } catch (NullPointerException e) {
            return Optional.empty();
        }

        URI uriLink = URI.create(clearLinkData.getLink());
        BaseSiteClient client = sitesMap.getClient(uriLink.getHost());
        BaseParseResponse parseResponse = new GeneralParseLink().start(clearLinkData.getLink()); // парсим ссылку и получаем необходимые поля

        OffsetDateTime updatedDate = client.getUpdatedDate(parseResponse);
        OffsetDateTime dbUpdatedDate = clearLinkData.getPageUpdatedDate();
        if (dbUpdatedDate.equals(updatedDate)) // если время обновлений совпадает, то выходим
            return Optional.empty();

        Map<String, String> responseDataChanges = client.getUpdates(parseResponse); // Обновленные данный из апи
        Map<String, String> dataChanges = clearLinkData.getDataChanges(); // Данные из бд, которые отслеживаем у ссылки

        String botText = generateBotMessage(responseDataChanges, dataChanges);

        linkService.updateDataChanges(responseDataChanges, updatedDate, clearLinkData.getId()); // записываю обновленные данные в бд
        return Optional.of(
                new LinkUpdateRequest(
                        clearLinkData.getId(),
                        uriLink,
                        botText,
                        chatLinkService.getAllChat(clearLinkData.getId())
                )
        );
    }

    private Link getOldestsLink() {
        return linkService.getOldestUpdateLink()
                .orElseThrow(NullPointerException::new); // если нет ни одной ссылки в бд
    }

    private String generateBotMessage(Map<String, String> responseDataChanges, Map<String, String> dataChanges) {
        StringBuilder text = new StringBuilder("Есть обновление"); // генерирую сообщение пользователю
        for (String key : responseDataChanges.keySet()) {
            if (dataChanges.get(key) != null && !Objects.equals(dataChanges.get(key), responseDataChanges.get(key))) {
                text.append(String.format("\n~~%s: %s~~ —> %s: %s",
                        key,
                        dataChanges.get(key),
                        key,
                        responseDataChanges.get(key)));
            }
        }
        return text.toString();
    }
}
