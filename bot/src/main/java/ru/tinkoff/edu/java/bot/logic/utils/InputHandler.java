package ru.tinkoff.edu.java.bot.logic.utils;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.commands.*;

import java.util.HashMap;

public class InputHandler {
    private static final HashMap<String, BaseCommand> COMMANDS = InitBaseCommands.getCommands();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = InitReplyCommands.getCommands();

    public SendMessage run(Update update) {
        Message message = update.message();
        BaseCommand baseCommand = COMMANDS.get(message.text());

        try {
            return baseCommand.execute(message);
        } catch (NullPointerException e) {
            return checkReplyMessage(message);
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
