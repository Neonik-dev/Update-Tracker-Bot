package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import ru.tinkoff.edu.java.bot.exceptions.InvalidLinkException;
import ru.tinkoff.edu.java.bot.exceptions.NotUniqueLinkException;
import ru.tinkoff.edu.java.bot.logic.utils.LinkValidation;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

@Getter
public class TrackCommand implements BaseCommand, ReplyCommand {
    public static final String REPLY = "Напишите ссылку, которую хотите начать отслеживать";
    private static final String FINISH_TEXT = "Ссылка успешно добавилась";

    @Override
    public SendMessage execute(Message message) {
        return SendSimpleMessage.create(message, REPLY, true);
    }

    @Override
    public SendMessage executeReply(Message message) {
        String text;
        try {
            ManagerCollection.add(LinkValidation.validate(message.text()));
            text = FINISH_TEXT;
        } catch (NotUniqueLinkException | InvalidLinkException e) {
            text = e.getMessage();
        }
        return SendSimpleMessage.create(message, text);
    }
}
