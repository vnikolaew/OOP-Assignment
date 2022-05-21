package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.results.*;

import java.io.IOException;

public final class CLIApp {

    private final IODevice ioDevice;
    private final CLIRequestFactory requestFactory;

    private static final String newLine = System.lineSeparator();

    public CLIApp(IODevice ioDevice, CLIRequestFactory factory) {
        this.ioDevice = ioDevice;
        this.requestFactory = factory;
    }

    public void run() {
        while (true) {
            processRequest();
        }
    }

    private void processRequest() {
        String input = readUserInput();

        CLIRequest cliRequest = requestFactory.createRequest(input);
        RequestResult result = cliRequest.execute();

        outputResult(result);
    }

    private void outputResult(RequestResult result) {
        try {
            ioDevice.write(result.getResultMessage() + newLine);
            if(result instanceof QueryResult<?>) {
                Object data = ((QueryResult<?>) result).getData();

                if(data != null) {
                    ioDevice.write(((QueryResult<?>) result).getData() + newLine);
                }
            }
        } catch (IOException ignored) { }
    }

    private String readUserInput() {
        final String promptSymbol = "> ";

        try {
            ioDevice.write(promptSymbol);
            return ioDevice.read().trim();
        } catch (IOException e) {
            return null;
        }
    }

}
