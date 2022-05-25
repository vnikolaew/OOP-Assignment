package com.vnikolaev.commands;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * A command for creating a new JSON element in the current JSON object.
 * Expected usage: create <path> <jsonString>
 */
public class CreateCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public CreateCommand(JSONDataSource dataSource, String[] args) {
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

        DataSourceOperationResult result = dataSource
                .createElement(path, payload);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
