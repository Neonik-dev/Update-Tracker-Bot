package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_link")
@Data
@NoArgsConstructor
public class ChatLink {
    @EmbeddedId
    private ChatLinkPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatId")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("linkId")
    private Link link;

    public ChatLink(Long chatId, Long linkId) {
        this.id = new ChatLinkPK(chatId, linkId);
    }

    public ChatLink(Chat chat, Link link) {
        this(chat.getId(), link.getId());
        this.chat = chat;
        this.link = link;
    }

    public ChatLink(ChatLinkPK chatLinkPK) {
        this.id = chatLinkPK;
    }

    public Long getChatId() {
        return id.getChatId();
    }

    public Long getLinkId() {
        return id.getLinkId();
    }
}
