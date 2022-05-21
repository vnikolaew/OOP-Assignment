package com.vnikolaev.results;

public abstract class RequestResult {

    protected final String resultMessage;
    protected final boolean success;

    public RequestResult(String resultMessage, boolean success) {
        this.resultMessage = resultMessage;
        this.success = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}
