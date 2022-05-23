package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.JSONDataSourceImpl;
import com.vnikolaev.datasource.conversions.JSONConverterImpl;
import com.vnikolaev.io.*;

/**
 * As the name implies, this class is responsible for hiding the complexity
 * of wiring up all the application's components together behind a simple
 * interface.
 */
public class CLIAppFacade {

    public void run(String[] args) {
        String currentDirectory = args.length == 0
                ? FileNameConstants.MainFilesDirectory
                : args[0];

        CLIApp app = buildApplication(currentDirectory);
        app.run();
    }

    private CLIApp buildApplication(String currentDirectory) {
        IODevice consoleIO = new ConsoleIO();
        FileIODevice fileIO = new FileIO();

        JSONConverter converter = new JSONConverterImpl();
        JSONDataSource dataSource = new JSONDataSourceImpl(fileIO, converter);

        ((JSONDataSourceImpl) dataSource).setCurrentDirectory(currentDirectory);

        CLIRequestFactory requestFactory = new CLIRequestFactoryImpl(dataSource);

        return new CLIApp(consoleIO, requestFactory);
    }
}
