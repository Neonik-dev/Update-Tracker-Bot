package ru.tinkoff.edu.java.bot.logic.commands.enums;

import lombok.Getter;
import ru.tinkoff.edu.java.bot.logic.commands.ReplyCommand;
import ru.tinkoff.edu.java.bot.logic.commands.TrackCommand;
import ru.tinkoff.edu.java.bot.logic.commands.UntrackCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum InitReplyCommands {
    TRACK(TrackCommand.REPLY, new TrackCommand()),
    UNTRACK(UntrackCommand.REPLY, new UntrackCommand());

    private final String name;
    private final ReplyCommand command;

    private static final class GuaranteeInit {
        static final Map<String, ReplyCommand> COMMANDS = new HashMap<>();
    }

    static {
        Arrays.stream(values()).forEach((value) -> GuaranteeInit.COMMANDS.put(value.name, value.command));
    }

    InitReplyCommands(String name, ReplyCommand command) {
        this.name = name;
        this.command = command;
    }

    public static Map<String, ReplyCommand> getCommands() {
        return GuaranteeInit.COMMANDS;
    }
}

