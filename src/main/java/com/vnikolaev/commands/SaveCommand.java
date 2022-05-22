package com.vnikolaev.commands;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * Represents a command for saving all the currently pending changes to the
 * JSON object and persisting them to the file.
 * Expected usage: save
 */
public class SaveCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public SaveCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    public int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected CommandResult executeCore() {
        DataSourceOperationResult result = dataSource.save();

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
