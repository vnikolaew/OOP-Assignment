package com.vnikolaev.queries;

import com.vnikolaev.abstractions.CLIQuery;
import com.vnikolaev.abstractions.JSONDataSource;
import com.vnikolaev.results.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

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
