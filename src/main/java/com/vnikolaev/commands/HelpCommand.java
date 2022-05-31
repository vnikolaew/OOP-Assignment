package com.vnikolaev.commands;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.requestdescriptors.*;
import com.vnikolaev.results.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A query for displaying helpful user information about the all the
 * application's requests with their descriptions and expected usages.
 */
public class HelpCommand extends CLIQuery<String> {

    private final RequestDescriptionFormatter formatter;

    private static final List<RequestDescriptor> descriptors;

    static {
        descriptors = new ArrayList<>(List.of(
                    new OpenCommandDescriptor(),
                    new CloseCommandDescriptor(),
                    new SaveCommandDescriptor(),
                    new SaveAsCommandDescriptor(),
                    new ChangeDirectoryCommandDescriptor(),
                    new PrintQueryDescriptor(),
                    new SearchQueryDescriptor(),
                    new UpdateCommandDescriptor(),
                    new CreateCommandDescriptor(),
                    new DeleteCommandDescriptor(),
                    new ValidateCommandDescriptor(),
                    new HelpCommandDescriptor(),
                    new ExitCommandDescriptor()));
    }

    public HelpCommand(RequestDescriptionFormatter formatter) {
        super(new String[0]);
        this.formatter = formatter;
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected QueryResult<String> executeCore() {
        StringBuilder info = new StringBuilder();
        info.append("The following commands are currently supported: \n");

        for(RequestDescriptor descriptor : descriptors) {
            info.append(formatter.formatLine(descriptor));
        }

        return QueryResult.success(info.toString(), "\n");
    }
}
