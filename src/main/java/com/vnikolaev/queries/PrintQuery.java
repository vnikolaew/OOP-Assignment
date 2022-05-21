package com.vnikolaev.queries;

import com.vnikolaev.abstractions.CLIQuery;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.results.QueryResult;

import java.util.Arrays;
import java.util.List;

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
    public int getRequiredArgumentsLength() {
        return 0;
    }
}
