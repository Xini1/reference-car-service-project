package com.andersenlab.carservice.application.command;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public final class Help extends NamedCommandWithDescription {

    private final Collection<Command> commands;

    public Help(Collection<? extends Command> commands) {
        super("help");
        this.commands = List.copyOf(commands);
    }

    @Override
    void executeIfMatched(PrintStream output, List<String> arguments) {
        commands.forEach(command -> command.printDescription(output));
        printDescription(output);
    }

    @Override
    List<String> expectedArguments() {
        return List.of();
    }

    @Override
    String description() {
        return "print all available commands";
    }
}
