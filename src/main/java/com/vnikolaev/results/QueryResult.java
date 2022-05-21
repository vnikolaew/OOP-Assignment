package com.vnikolaev.results;

public class QueryResult<T> extends RequestResult {

    private final T data;

    public QueryResult(String resultMessage, boolean success, T data) {
        super(resultMessage, success);
        this.data = data;
    }

    public static <T> QueryResult<T> success(T data) {
        return new QueryResult<>(null, true, data);
    }

    public static <T> QueryResult<T> success(T data, String message) {
        return new QueryResult<>(message, true, data);
    }

    public static <T> QueryResult<T> failure(
            String message, T data) {
        return new QueryResult<>(message, false, data);
    }

    public static QueryResult<?> failure(String message) {
        return new QueryResult<>(message, false, null);
    }

    public T getData() {
        return data;
    }
}
