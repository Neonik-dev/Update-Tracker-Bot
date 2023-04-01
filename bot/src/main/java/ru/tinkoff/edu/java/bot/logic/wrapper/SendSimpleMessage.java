package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public class SendSimpleMessage extends SendMessage {
    public SendSimpleMessage(Object chatId, String text) {
        super(chatId, text);
        System.out.println(this);
//        this.parseMode(ParseMode.MarkdownV2);
    }

    public SendSimpleMessage(Message message, String text) {
        super(message.chat().id(), text);
    }
    public SendSimpleMessage(Message message, String text, Boolean isReply) {
        this(message, text);
    }
}
