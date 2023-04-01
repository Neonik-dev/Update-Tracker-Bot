package ru.tinkoff.edu.java.bot.logic.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.commands.*;

import java.util.HashMap;
import java.util.List;

public class InputHandler {
    private static final HashMap<String, BaseCommand> COMMANDS = InitBaseCommands.getCommands();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = InitReplyCommands.getCommands();
//    private final ScrapperClient scrapperClient;

    public void run(TelegramBot bot, List<Update> updates) {
        Message message;
        SendMessage sendMessage;

        for (Update update : updates) {
            message = update.message();
            BaseCommand baseCommand = COMMANDS.get(message.text());

            try {
                sendMessage = baseCommand.execute(message);
            } catch (NullPointerException e) {
                sendMessage = checkReplyMessage(message);
            }
            bot.execute(sendMessage);
        }
    }

    private SendMessage checkReplyMessage(Message message) {
        try {
            ReplyCommand replyCommand = REPLY_COMMANDS.get(message.replyToMessage().text());
            return replyCommand.executeReply(message);
        } catch (NullPointerException e){
            return InvalidCommand.execute(message);
        }
    }
}
