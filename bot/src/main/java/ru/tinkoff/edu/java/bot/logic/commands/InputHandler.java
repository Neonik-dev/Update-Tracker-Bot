package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    private static Map<String, BaseCommand> commands = new HashMap<>();
    private static Map<String, ReplyCommand> replyCommands = new HashMap<>();

    public InputHandler(InitCommands initCommands) {
        if (commands.isEmpty()) {
            commands = initCommands.getAllCommands();
            replyCommands = initCommands.getReplyCommands();
        }
    }

    public SendMessage run(Update update) {
        Message message = update.message();
        try {
            BaseCommand baseCommand = commands.get(message.text());
            return baseCommand.execute(message);
        } catch (NullPointerException e) {
            return checkReplyMessage(message);
        } catch (Throwable e) {
            return SendSimpleMessage.create(
                    message.chat().id(),
                    "Возникли небольшие неполадки! ;( \n Попробуйте чуть позже"
            );
        }
    }

    private SendMessage checkReplyMessage(Message message) {
        try {
            ReplyCommand replyCommand = replyCommands.get(message.replyToMessage().text());
            return replyCommand.executeReply(message);
        } catch (NullPointerException e) {
            return InvalidCommand.execute(message);
        }
    }
}
