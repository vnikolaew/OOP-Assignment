package com.vnikolaev.abstractions;

import com.vnikolaev.results.RequestResult;

/**
 * Represents the base interface for all the application's requests
 * sent by the user.
 */
public interface CLIRequest {
    RequestResult execute();
}
