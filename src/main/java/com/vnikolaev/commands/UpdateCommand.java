package com.vnikolaev.commands;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * Represents an update command that will find the JSON object specified
 * by the provided path and updated it with a new value provided by the payload
 * argument. Note that if JSON conversion for the payload fails, then this
 * command will return failure as well and no changes will be made.
 * Expected usage: set <path> <payload>
 */
public class UpdateCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public UpdateCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 2;
    }

    @Override
    protected CommandResult executeCore() {
        String path = args[0];
        String payload = args[1];

        if(path == null || payload == null) {
            return CommandResult.failure("Invalid command parameters");
        }

        DataSourceOperationResult result = dataSource.setElement(path, payload);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
