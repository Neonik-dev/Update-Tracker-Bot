package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class SendSimpleMessage {
    public static SendMessage create(Long chatId, String text) {
        return new SendMessage(chatId, text).parseMode(ParseMode.Markdown);
    }

    public static SendMessage create(Message message, String text) {
        return create(message.chat().id(), text);
    }

    public static SendMessage create(Message message, String text, Boolean isReply) {
        SendMessage sendMessage = create(message.chat().id(), text);
        if (isReply)
            sendMessage.replyMarkup(new ForceReply());
        return sendMessage;
    }
}
