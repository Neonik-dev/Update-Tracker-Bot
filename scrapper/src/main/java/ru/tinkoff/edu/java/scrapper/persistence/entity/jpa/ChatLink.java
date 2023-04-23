package ru.tinkoff.edu.java.scrapper.persistence.entity.jpa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_link")
@Data
//@IdClass(ChatLinkPK.class)
@NoArgsConstructor
public class ChatLink {
    public ChatLink(Chats chat, Links link) {
        this.chat = chat;
        this.link = link;
        this.id = new ChatLinkPK(chat.getId(), link.getId());
    }

    @EmbeddedId
    private ChatLinkPK id;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="chat_id", referencedColumnName = "id", nullable = false)
    @MapsId("chatId")
    private Chats chat;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="link_id", referencedColumnName = "id", nullable = false)
    @MapsId("linkId")
    private Links link;
}
