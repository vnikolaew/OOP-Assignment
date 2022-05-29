package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.results.*;

import java.io.IOException;

/**
 * Represents the application's main entry point.
 */
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

    /**
     * The core method responsible for taking in user input, interpreting it to the
     * appropriate request and executing it against the underlying data source. At
     * the end it displays the result of the execution.
     */
    private void processRequest() {
        String input = readUserInput();

        CLIRequest cliRequest = requestFactory.createRequest(input);
        RequestResult result = cliRequest.execute();

        outputResult(result);
    }

    /**
     * A method for displaying the result of the user request that
     * was sent on the output device. Note that it also displays
     * additional data if the user made a query request.
     */
    private void outputResult(RequestResult result) {
        try {
            ioDevice.write(result.getResultMessage() + newLine);
            if(result instanceof QueryResult<?>) {
                Object data = ((QueryResult<?>) result).getData();

                if(data != null) {
                    ioDevice.write(data + newLine);
                }
            }
        } catch (IOException ignored) { }
    }

    /**
     * A method for reading user input from the input device.
     */
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