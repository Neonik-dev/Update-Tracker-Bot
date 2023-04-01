package ru.tinkoff.edu.java.bot.logic.commands;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum InitReplyCommands {
    TRACK(TrackCommand.REPLY, new TrackCommand()),
    UNTRACK(UntrackCommand.REPLY, new UntrackCommand());

    private final String name;
    private final ReplyCommand command;

    private static final class GuaranteeInit {
        static final HashMap<String, ReplyCommand> COMMANDS = new HashMap<>();
    }

    static {
        for (InitReplyCommands command : values()) {
            GuaranteeInit.COMMANDS.put(command.getName(), command.getCommand());
        }
    }

    InitReplyCommands(String name, ReplyCommand command) {
        this.name = name;
        this.command = command;
    }

    public static HashMap<String, ReplyCommand> getCommands() {
        return GuaranteeInit.COMMANDS;
    }
}

