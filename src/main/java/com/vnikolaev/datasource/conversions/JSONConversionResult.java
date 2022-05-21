package com.vnikolaev.datasource.conversions;

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