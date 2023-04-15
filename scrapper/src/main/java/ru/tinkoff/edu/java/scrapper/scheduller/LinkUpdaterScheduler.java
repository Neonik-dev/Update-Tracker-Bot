package ru.tinkoff.edu.java.scrapper.scheduller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.BaseSiteClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.enums.SiteEnum;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final Map<String, BaseSiteClient> SITES = new HashMap<>();
    private final LinkService linkService;

    {
        Arrays.stream(SiteEnum.values()).forEach((value) -> SITES.put(value.getDomain(), value.getClient()));
    }

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        Optional<LinkData> linkData = linkService.getOldestUpdateLink();
        if (linkData.isPresent()) {
            // пока захардкодил здесь. Получается на этом этапе нужно образаться в модуль link-parse?
            // если да, то как мне его сюда внедрить? как-то инжектить бин?
            BaseResponse response = SITES.get("github.com").getUpdates("Neonik228", "Tinkoff_project");
            log.info("Updating the link on a schedule " + linkData.get().getId());
        }
    }
}
