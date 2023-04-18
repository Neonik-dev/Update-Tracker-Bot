package ru.tinkoff.edu.java.scrapper.scheduller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.clients.clients.TgBotClient;
import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.service.GenerateUpdatesService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final GenerateUpdatesService generateUpdatesService;
    private final TgBotClient botClient;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Updating the link on a schedule");
        Optional<LinkUpdateRequest> request = generateUpdatesService.getUpdates();
        request.ifPresent(botClient::postUpdates);
    }
}
