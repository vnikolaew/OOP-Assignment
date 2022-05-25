package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.results.CommandResult;

/**
 * A command for deleting an existing JSON element (if any) in the current
 * JSON object. Expected usage: delete <path>
 */
public class DeleteCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public DeleteCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 1;
    }

    @Override
    protected CommandResult executeCore() {
        String path = args[0];

        if(path == null) {
            return CommandResult.failure("Invalid command parameters");
        }

        DataSourceOperationResult result = dataSource.deleteElement(path);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
