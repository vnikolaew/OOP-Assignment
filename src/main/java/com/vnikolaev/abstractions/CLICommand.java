package com.vnikolaev.abstractions;

import com.vnikolaev.results.CommandResult;

public abstract class CLICommand implements CLIRequest {

    protected final String[] args;

    protected CLICommand(String[] args) {
        this.args = args;
    }

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
