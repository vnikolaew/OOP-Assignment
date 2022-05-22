package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * A command for closing the currently opened file (if any).
 * Expected usage: close <fileName>
 */
public class CloseCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public CloseCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    public int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected CommandResult executeCore() {
        DataSourceOperationResult result = dataSource.close();

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
