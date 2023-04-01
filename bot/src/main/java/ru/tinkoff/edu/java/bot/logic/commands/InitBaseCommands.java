package ru.tinkoff.edu.java.bot.logic.commands;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum InitBaseCommands {
    START("/start", new StartCommand()),
    HELP("/help", new HelpCommand()),
    LIST("/list", new ListCommand()),
    TRACK("/track", new TrackCommand()),
    UNTRACK("/untrack", new UntrackCommand());

    private final String name;
    private final BaseCommand command;
    private static final class GuaranteeInit {
        static final HashMap<String, BaseCommand> COMMANDS = new HashMap<>();
    }

    static {
        for (InitBaseCommands command : values()) {
            GuaranteeInit.COMMANDS.put(command.getName(), command.getCommand());
        }
    }

    InitBaseCommands(String name, BaseCommand command) {
        this.name = name;
        this.command = command;
    }

    public static HashMap<String, BaseCommand> getCommands() {
        return GuaranteeInit.COMMANDS;
    }
}
