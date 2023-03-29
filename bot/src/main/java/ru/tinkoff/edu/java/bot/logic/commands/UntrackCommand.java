package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.exceptions.NotUniqueLinkException;
import ru.tinkoff.edu.java.bot.logic.utils.LinkValidation;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;

public class UntrackCommand implements BaseCommand, ReplyCommand{
    private static final String REPLY = "Напишите ссылку, отслеживание которой хотите прекратить";
    private final String name = "/untrack";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " -> прекращает отслеживание ссылки";
    }

    @Override
    public SendMessage execute(Message message) {
        return new SendMessage(message.chat().id(), REPLY).replyMarkup(new ForceReply());
    }

    @Override
    public SendMessage executeReply(Message message) {
        String text;
        try {
            ManagerCollection.remove(LinkValidation.validate(message.text()));
            text = "Ссылка успешно удалена из списка";
        } catch (NotUniqueLinkException e) {
            text = e.getMessage();
        }
        return new SendMessage(message.chat().id(), text);
    }

    @Override
    public String getReply() {
        return REPLY;
    }
}
