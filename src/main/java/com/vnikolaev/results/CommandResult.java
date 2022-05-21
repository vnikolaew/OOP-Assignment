package com.vnikolaev.results;

public class CommandResult extends RequestResult {

    public CommandResult(String resultMessage, boolean success) {
        super(resultMessage, success);
    }

    public static CommandResult success(String successMessage) {
        return new CommandResult(successMessage, true);
    }

    public static CommandResult failure(String failureMessage) {
        return new CommandResult(failureMessage, false);
    }
}
