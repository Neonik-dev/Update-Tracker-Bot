package ru.tinkoff.edu.java.bot.logic.commands;

import java.util.HashMap;

public class Commands {
    private static final HashMap<String, BaseCommand> COMMANDS = new HashMap<>();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = new HashMap<>();

    public Commands() {
        if (COMMANDS.isEmpty()) {
            for(InitBaseCommands command : InitBaseCommands.values()) {
                COMMANDS.put(command.getName(), command.getCommand());
            }
        }
        if (REPLY_COMMANDS.isEmpty()) {
            for(InitReplyCommands command : InitReplyCommands.values()) {
                REPLY_COMMANDS.put(command.getName(), command.getCommand());
            }
        }
    }

    public static HashMap<String, BaseCommand> getBaseCommands() {
        return COMMANDS;
    }
    public static HashMap<String, ReplyCommand> getReplyCommands() {
        return REPLY_COMMANDS;
    }
}
