package com.vnikolaev.queries;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.results.QueryResult;

/**
 * Represents a query request for displaying the entire JSON object in a
 * friendly formatted string.
 * Expected usage: print
 */
public class PrintQuery extends CLIQuery<String> {

    private final JSONDataSource dataSource;

    public PrintQuery(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected QueryResult<String> executeCore() {
        String content = dataSource.toFriendlyString();

        return content == null
                ? QueryResult.failure("No content available", null)
                :  QueryResult.success(content, "JSON schema: \n");
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 0;
    }
}
