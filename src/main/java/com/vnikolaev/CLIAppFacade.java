package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.*;
import com.vnikolaev.io.*;
import com.vnikolaev.abstractions.JSONPathInterpreter;
import com.vnikolaev.datasource.conversions.JSONConverterImpl;
import com.vnikolaev.datasource.pathinterpretors.ModernJSONPathInterpreter;

/**
 * As the name implies, this class is responsible for hiding the complexity
 * of wiring up all the application's components together behind a simple
 * interface.
 */
public class CLIAppFacade {

    public void run(String[] args) {
        String currentDirectory = args.length == 0
                ? FileNameConstants.javaDirectory
                : args[0];

        CLIApp app = buildApplication(currentDirectory);
        app.run();
    }

    private CLIApp buildApplication(String currentDirectory) {
        IODevice consoleIO = new ConsoleIO();
        FileIODevice fileIO = new FileIO();

        JSONPathInterpreter pathInterpreter = new ModernJSONPathInterpreter();
        JSONConverter converter = new JSONConverterImpl();

        JSONDataSource dataSource = new JSONDataSourceImpl(fileIO, converter, pathInterpreter);
        dataSource.changeDirectory(currentDirectory);

        CLIRequestFactory requestFactory = new CLIRequestFactoryImpl(dataSource);

        return new CLIApp(consoleIO, requestFactory);
    }
}
