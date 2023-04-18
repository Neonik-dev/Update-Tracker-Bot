package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import ru.tinkoff.edu.java.responses.BaseParseResponse;

import java.util.Map;

public interface BaseSiteClient {
    Map<String, String> getUpdates(BaseParseResponse response);
}
