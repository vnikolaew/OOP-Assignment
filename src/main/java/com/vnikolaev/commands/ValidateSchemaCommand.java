package com.vnikolaev.commands;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

/**
 * Represents a command for validating the schema of the current JSON
 * object. Expected usage: validate
 */
public class ValidateSchemaCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public ValidateSchemaCommand(JSONDataSource dataSource) {
        super(new String[0]);
        this.dataSource = dataSource;
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected CommandResult executeCore() {
        DataSourceOperationResult result = dataSource.validateSchema();

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }
}
