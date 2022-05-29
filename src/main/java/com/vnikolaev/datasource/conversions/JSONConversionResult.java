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

    /**
     * @param data The data being returned from the conversion.
     * A static factory method for creating a successful result
     * from the conversion.
     */
    public static <E> JSONConversionResult<E> success(E data) {
        return new JSONConversionResult<>(data, null);
    }

    /**
     * A static factory method for creating a failed result
     * from the conversion with an error message.
     */
    public static <E> JSONConversionResult<E> failure(String error) {
        return new JSONConversionResult<>(null, error);
    }
}