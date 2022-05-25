package com.vnikolaev.abstractions;

/**
 * An abstraction over the underlying request factory the application
 * is going to work with. It exposes only a single method for
 * translating a client's request to a corresponding CLI command / query.
 */
public interface CLIRequestFactory {
    CLIRequest createRequest(String requestString);
}
