package ru.tinkoff.edu.java.scrapper.persistence.entity.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ChatLinkPK implements Serializable {
    private Long chatId;
    private Long linkId;
}
