package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.dto.BaseSiteResponse;

public interface BaseSiteClient {
    BaseSiteResponse getUpdates(BaseParseResponse response);
}
