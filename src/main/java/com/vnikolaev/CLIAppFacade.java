package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.JSONDataSourceImpl;
import com.vnikolaev.datasource.conversions.JSONConverterImpl;
import com.vnikolaev.io.*;

public class CLIAppFacade {

    public void run(String[] args) {
        String filePath = args.length == 0
                ? FileNameConstants.MainFilesDirectory
                : args[0];

        CLIApp app = buildApplication(filePath);
        app.run();
    }

    private CLIApp buildApplication(String filePath) {
        IODevice consoleIO = new ConsoleIO();
        FileIODevice fileIO = new FileIO();

        JSONConverter converter = new JSONConverterImpl();
        JSONDataSource dataSource = new JSONDataSourceImpl(fileIO, converter);

        ((JSONDataSourceImpl) dataSource).setBaseDirectory(filePath);

        CLIRequestFactory requestFactory = new CLIRequestFactoryImpl(dataSource);

        return new CLIApp(consoleIO, requestFactory);
    }
}
