package com.vnikolaev.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a result object of how the operation against the data source has
 * gone. In case of a successful one, the success message will be populated,
 * else a list of one or more errors will be created.
 */
public class DataSourceOperationResult {

    /**
     * A list of possible errors that may have occurred during the
     * operation
     */
    private final List<String> errors;

    /**
     * A short message in case of an operation's successful execution
     */
    private final String successMessage;

    private DataSourceOperationResult(List<String> errors, String result) {
        this.errors = errors;
        this.successMessage = result;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public static DataSourceOperationResult success(String message) {
        return new DataSourceOperationResult(new ArrayList<>(), message);
    }

    public static DataSourceOperationResult failure(List<String> errors) {
        return new DataSourceOperationResult(errors, null);
    }

    /**
     * @return Returns whether the operation was executed successfully
     */
    public boolean isSuccessful() {
        return errors == null
                || errors.size() == 0
                || successMessage != null;
    }

    /**
     * @return Returns whether the operation failed execution
     */
    public boolean hasFailed() {
        return !isSuccessful();
    }

}
