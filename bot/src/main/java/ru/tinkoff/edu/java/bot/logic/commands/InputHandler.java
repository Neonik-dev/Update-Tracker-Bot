package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    private static Map<String, BaseCommand> COMMANDS = new HashMap<>();
    private static Map<String, ReplyCommand> REPLY_COMMANDS =  new HashMap<>();

    public InputHandler(InitCommands initCommands) {
        if (COMMANDS.isEmpty()) {
            COMMANDS = initCommands.getAllCommands();
            REPLY_COMMANDS = initCommands.getReplyCommands();
        }
    }

    public SendMessage run(Update update) {
        Message message = update.message();
        try {
            BaseCommand baseCommand = COMMANDS.get(message.text());
            return baseCommand.execute(message);
        } catch (NullPointerException e) {
            return checkReplyMessage(message);
        } catch (Throwable e) {
            return SendSimpleMessage.create(message.chat().id(), "Возникли небольшие неполадки! ;( \n Попробуйте чуть позже");
        }
    }

    private SendMessage checkReplyMessage(Message message) {
        try {
            ReplyCommand replyCommand = REPLY_COMMANDS.get(message.replyToMessage().text());
            return replyCommand.executeReply(message);
        } catch (NullPointerException e) {
            return InvalidCommand.execute(message);
        }
    }
}
