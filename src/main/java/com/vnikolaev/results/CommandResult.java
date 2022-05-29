package com.vnikolaev.results;

/**
 * A response to a user sent command.
 */
public class CommandResult extends RequestResult {

    public CommandResult(String resultMessage, boolean success) {
        super(resultMessage, success);
    }

    /**
     * A static factory method for creating a successful result.
     */
    public static CommandResult success(String successMessage) {
        return new CommandResult(successMessage, true);
    }

    /**
     * A static factory method for creating a failed result.
     */
    public static CommandResult failure(String failureMessage) {
        return new CommandResult(failureMessage, false);
    }
}
