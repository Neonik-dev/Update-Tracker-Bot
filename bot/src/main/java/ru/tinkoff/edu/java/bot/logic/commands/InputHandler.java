package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.commands.enums.InitBaseCommands;
import ru.tinkoff.edu.java.bot.logic.commands.enums.InitReplyCommands;

import java.util.Map;

public class InputHandler {
    private static final Map<String, BaseCommand> COMMANDS = InitBaseCommands.getCommands();
    private static final Map<String, ReplyCommand> REPLY_COMMANDS = InitReplyCommands.getCommands();

    public SendMessage run(Update update) {
        Message message = update.message();
        try {
            BaseCommand baseCommand = COMMANDS.get(message.text());
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
