package com.vnikolaev.queries;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.results.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a search query request for finding a specific JSON element
 * matched by the specified path.
 * Expected usage: search <key>
 */
public class SearchQuery extends CLIQuery<String> {

    private final JSONDataSource dataSource;

    public SearchQuery(JSONDataSource dataSource, String[] args) {
        super(args);
        this.dataSource = dataSource;
    }

    @Override
    protected QueryResult<String> executeCore() {
        String searchKey = args[0];

        List<?> result = dataSource.searchElement(searchKey);
        if(result == null) {
            return QueryResult.failure("Could not execute a search.", null);
        }

         var resultString = result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return QueryResult.success("[ " + resultString + " ]", "Search result: ");
    }

    @Override
    public int getRequiredArgumentsLength() {
        return 1;
    }
}
