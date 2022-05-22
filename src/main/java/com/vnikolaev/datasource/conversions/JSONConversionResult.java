package com.vnikolaev.datasource.conversions;

/**
 * Represents a result about how a conversion from / to a JSON
 * object has gone.
 * @param <T> The result data type.
 */
public record JSONConversionResult<T>(T data, String error) {
    public boolean isSuccessful() {
        return data != null;
    }

    public static <E> JSONConversionResult<E> success(E data) {
        return new JSONConversionResult<>(data, null);
    }

    public static <E> JSONConversionResult<E> failure(String error) {
        return new JSONConversionResult<>(null, error);
    }
}