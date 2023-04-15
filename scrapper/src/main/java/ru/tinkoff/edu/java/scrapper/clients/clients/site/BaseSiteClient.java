package ru.tinkoff.edu.java.scrapper.clients.clients.site;

import ru.tinkoff.edu.java.scrapper.clients.dto.BaseResponse;

public interface BaseSiteClient {
    BaseResponse getUpdates(String user, String repo);
}
