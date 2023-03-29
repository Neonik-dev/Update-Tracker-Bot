package ru.tinkoff.edu.java.bot.logic.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.commands.BaseCommand;
import ru.tinkoff.edu.java.bot.logic.commands.Commands;
import ru.tinkoff.edu.java.bot.logic.commands.InvalidCommand;
import ru.tinkoff.edu.java.bot.logic.commands.ReplyCommand;

import java.util.HashMap;
import java.util.List;

public class InputHandler {
    private static final HashMap<String, BaseCommand> COMMANDS = new Commands().getBaseCommands();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = new Commands().getReplyCommands();

    public void run(TelegramBot bot, List<Update> updates) {
        Message message;
        SendMessage sendMessage;
        for (Update update : updates) {
            message = update.message();

            BaseCommand baseCommand = COMMANDS.get(message.text().trim());
            if (baseCommand != null) {
                sendMessage = baseCommand.execute(message);
            } else if (message.replyToMessage() != null) {
                ReplyCommand replyCommand = REPLY_COMMANDS.get(message.replyToMessage().text());
                if (replyCommand == null) {
                    sendMessage = InvalidCommand.execute(message);
                } else {
                    sendMessage = replyCommand.executeReply(message);
                }
            } else {
                sendMessage = InvalidCommand.execute(message);
            }
            bot.execute(sendMessage);
        }
    }
}
