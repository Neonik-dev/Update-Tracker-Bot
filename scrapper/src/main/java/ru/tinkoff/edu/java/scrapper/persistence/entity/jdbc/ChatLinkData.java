package ru.tinkoff.edu.java.scrapper.persistence.entity.jdbc;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatLinkData {
    private Long chatId;
    private Long linkId;
}
