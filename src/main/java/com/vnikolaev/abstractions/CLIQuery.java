package com.vnikolaev.abstractions;

import com.vnikolaev.results.QueryResult;

/**
 * Represents the base class for all the application's queries that
 * the user may search against.
 */
public abstract class CLIQuery<T> implements CLIRequest {

    protected final String[] args;

    protected CLIQuery(String[] args) {
        this.args = args;
    }

    public QueryResult<T> execute() {
        int argsReceived = args.length;
        int argsExpected = getRequiredArgumentsLength();

        return argsReceived == argsExpected
                ? executeCore()
                : QueryResult.failure("Invalid number of arguments. Got "
                + argsReceived + ", expected " + argsExpected + ".", null);
    }

    protected abstract QueryResult<T> executeCore();
    protected abstract int getRequiredArgumentsLength();
}
