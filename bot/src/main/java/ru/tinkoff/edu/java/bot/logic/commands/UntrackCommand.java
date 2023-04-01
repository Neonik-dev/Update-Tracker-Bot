package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import ru.tinkoff.edu.java.bot.exceptions.InvalidLinkException;
import ru.tinkoff.edu.java.bot.exceptions.NotUniqueLinkException;
import ru.tinkoff.edu.java.bot.logic.utils.LinkValidation;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;

@Getter
public class UntrackCommand implements BaseCommand, ReplyCommand{
    private final String description = "прекращает отслеживание ссылки";
    public static final String REPLY = "Напишите ссылку, отслеживание которой хотите прекратить";
    private static final String FINISH_TEXT = "Ссылка успешно удалена из списка";

    @Override
    public SendMessage execute(Message message) {
        return new SendMessage(message.chat().id(), REPLY).replyMarkup(new ForceReply());
//        return new SendSimpleMessage(message.chat().id(), REPLY).replyMarkup(new ForceReply());
    }

    @Override
    public SendMessage executeReply(Message message) {
        String text;
        try {
            ManagerCollection.remove(LinkValidation.validate(message.text()));
            text = FINISH_TEXT;
        } catch (NotUniqueLinkException | InvalidLinkException e) {
            text = e.getMessage();
        }
        return new SendMessage(message.chat().id(), text);
    }
}
