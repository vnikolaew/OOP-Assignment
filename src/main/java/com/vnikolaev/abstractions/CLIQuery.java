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

    /**
     * A template method for all application queries, checking whether
     * the supplied arguments are the right amount and returning a failed
     * result if not. Otherwise, it delegates the core logic to the derived
     * classes.
     */
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
