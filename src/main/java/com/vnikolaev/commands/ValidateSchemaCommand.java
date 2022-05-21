package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.results.CommandResult;

public class ValidateSchemaCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public ValidateSchemaCommand(JSONDataSource dataSource) {
        super(new String[0]);
        this.dataSource = dataSource;
    }

    @Override
    public int getRequiredArgumentsLength() {
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
