package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.exceptions.InvalidLinkException;
import ru.tinkoff.edu.java.bot.logic.utils.LinkValidation;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

@Getter
@RequiredArgsConstructor
public class UntrackCommand implements BaseCommand, ReplyCommand{
    private final ScrapperClient scrapperClient;
    public static final String REPLY = "Напишите ссылку, отслеживание которой хотите прекратить";
    private static final String FINISH_TEXT = "Ссылка успешно удалена из списка";
    private static final String NAME = "/untrack";

    @Override
    public SendMessage execute(Message message) {
        return SendSimpleMessage.create(message, REPLY, true);
    }

    @Override
    public SendMessage executeReply(Message message) {
        String text;
        try {
            System.out.println("Запросик отправляем");
            scrapperClient.deleteLink(message.chat().id(), LinkValidation.validate(message.text()));
//            ManagerCollection.remove(LinkValidation.validate(message.text()));
            text = FINISH_TEXT;
        } catch (InvalidLinkException e) {
            text = e.getMessage();
        }
        return SendSimpleMessage.create(message, text);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME + " -> прекращает отслеживание ссылки";
    }

    @Override
    public String getReply() {
        return REPLY;
    }
}
