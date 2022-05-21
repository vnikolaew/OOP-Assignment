package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.results.CommandResult;

public class MoveCommand extends CLICommand {

    private final JSONDataSource dataSource;

    public MoveCommand(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected CommandResult executeCore() {
        String fromPath = args[0];
        String toPath = args[1];

        if(fromPath == null || toPath == null) {
            return CommandResult.failure("Invalid command parameters");
        }

        DataSourceOperationResult result = dataSource
                .moveElements(fromPath, toPath);

        return result.isSuccessful()
                ? CommandResult.success(result.getSuccessMessage())
                : CommandResult.failure(result.getErrors().get(0));
    }

    @Override
    public int getRequiredArgumentsLength() {
        return 2;
    }
}
