package com.vnikolaev.datasource;

import java.util.ArrayList;
import java.util.List;

public class DataSourceOperationResult {

    private final List<String> errors;
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

    public boolean isSuccessful() {
        return errors == null
                || errors.size() == 0
                || successMessage != null;
    }

    public boolean hasFailed() {
        return !isSuccessful();
    }

}
