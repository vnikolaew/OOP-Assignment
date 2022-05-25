package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.results.CommandResult;

/**
 * Represents an invalid command issued by the user that is not supported
 * or is unavailable by the application.
 */
public class InvalidCommand extends CLICommand {

    public InvalidCommand() {
        super(new String[0]);
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected CommandResult executeCore() {
        return CommandResult.failure("Invalid / unsupported command. Type help to see all available commands.");
    }
}
