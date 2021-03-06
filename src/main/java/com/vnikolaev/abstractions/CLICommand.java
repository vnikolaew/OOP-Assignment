package com.vnikolaev.abstractions;

import com.vnikolaev.results.CommandResult;


/**
 * Represents the base class for all the application's commands that
 * are intended to change or write data -  sent by the user.
 */
public abstract class CLICommand implements CLIRequest {

    protected final String[] args;

    protected CLICommand(String[] args) {
        this.args = args;
    }

    /**
     * A template method for all application commands, checking whether
     * the supplied arguments are the right amount and returning a failed
     * result if not. Otherwise, it delegates the core logic to the derived
     * classes.
     */
    @Override
    public CommandResult execute() {
        int argsReceived = args.length;
        int argsExpected = getRequiredArgumentsLength();

        return argsReceived == argsExpected
                ? executeCore()
                : CommandResult.failure("Invalid number of arguments. Got "
                + argsReceived + ", expected " + argsExpected + ".");
    }

    protected abstract CommandResult executeCore();

    protected abstract int getRequiredArgumentsLength();
}
