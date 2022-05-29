package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * A command for changing the current file system directory
 * the user is located at.
 * Expected usage: cd <directoryName>
 */
public class ChangeDirectoryCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public ChangeDirectoryCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected CommandResult executeCore() {
        String newLocation = args[0];

        DataSourceOperationResult result
                = dataSource.changeDirectory(newLocation);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 1;
    }
}
