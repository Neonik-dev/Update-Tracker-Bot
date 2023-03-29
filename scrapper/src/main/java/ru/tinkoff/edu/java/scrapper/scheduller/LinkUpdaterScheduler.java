package ru.tinkoff.edu.java.scrapper.scheduller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Updating the link on a schedule");
    }
}
