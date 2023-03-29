package ru.tinkoff.edu.java.bot.logic.commands;

import java.util.HashMap;

public class Commands {
    private static final HashMap<String, BaseCommand> COMMANDS = new HashMap<>();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = new HashMap<>();

    public Commands() {
        if (COMMANDS.isEmpty()) {
            StartCommand startCommand = new StartCommand();
            COMMANDS.put(startCommand.getName(), startCommand);

            HelpCommand helpCommand = new HelpCommand();
            COMMANDS.put(helpCommand.getName(), helpCommand);

            TrackCommand trackCommand = new TrackCommand();
            COMMANDS.put(trackCommand.getName(), trackCommand);
            REPLY_COMMANDS.put(trackCommand.getReply(), trackCommand);

            UntrackCommand untrackCommand = new UntrackCommand();
            COMMANDS.put(untrackCommand.getName(), untrackCommand);
            REPLY_COMMANDS.put(untrackCommand.getReply(), untrackCommand);

            ListCommand listCommand = new ListCommand();
            COMMANDS.put(listCommand.getName(), listCommand);

        }
    }

    public HashMap<String, BaseCommand> getBaseCommands() {
        return COMMANDS;
    }
    public HashMap<String, ReplyCommand> getReplyCommands() {
        return REPLY_COMMANDS;
    }
}
