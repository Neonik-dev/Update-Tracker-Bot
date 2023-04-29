package ru.tinkoff.edu.java.bot.logic.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InitCommands {
    private static final Map<String, BaseCommand> COMMANDS = new HashMap<>();
    private static final HashMap<String, ReplyCommand> REPLY_COMMANDS = new HashMap<>();

    public InitCommands(StartCommand startCommand, TrackCommand trackCommand,
                        UntrackCommand untrackCommand, ListCommand listCommand) {
        if (COMMANDS.isEmpty()) {
            COMMANDS.put(startCommand.getName(), startCommand);

            HelpCommand helpCommand = new HelpCommand();
            COMMANDS.put(helpCommand.getName(), helpCommand);

            COMMANDS.put(trackCommand.getName(), trackCommand);
            REPLY_COMMANDS.put(trackCommand.getReply(), trackCommand);

            COMMANDS.put(untrackCommand.getName(), untrackCommand);
            REPLY_COMMANDS.put(untrackCommand.getReply(), untrackCommand);

            COMMANDS.put(listCommand.getName(), listCommand);
        }
    }

    public static Map<String, BaseCommand> getAllCommands() {
        return COMMANDS;
    }

    public HashMap<String, ReplyCommand> getReplyCommands() {
        return REPLY_COMMANDS;
    }
}
