package com.vnikolaev.commands;

import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.abstractions.*;
import com.vnikolaev.results.CommandResult;

/**
 * Represents a command for saving all the currently pending changes to the
 * JSON object and persisting them to a new file.
 * Expected usage: saveas <filePath>
 */
public class SaveAsCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public SaveAsCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    public int getRequiredArgumentsLength() {
        return 1;
    }

    @Override
    protected CommandResult executeCore() {
        String path = args[0];

        if(path == null) {
            return CommandResult.failure("Invalid command parameters");
        }

        DataSourceOperationResult result = dataSource.saveAs(path);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
