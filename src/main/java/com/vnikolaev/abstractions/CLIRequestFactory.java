package com.vnikolaev.abstractions;

public interface CLIRequestFactory {
    CLIRequest createRequest(String requestString);
}
