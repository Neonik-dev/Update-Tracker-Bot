package ru.tinkoff.edu.java.scrapper.clients.clients.notifier;

import ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest;

public interface SenderUpdatedLinks {
    void sendUpdates(LinkUpdateRequest linkUpdateRequest);
}
