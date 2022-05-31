package com.vnikolaev;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class that holds full absolute paths to commonly used directories
 */
public class FileNameConstants {
    public static final String baseDirectory =
            Path.of(Paths.get("").toAbsolutePath().toString())
            .toString();

    public static final String currentDirectory =
            Paths.get("").toAbsolutePath().toString();

    public static final String filesDirectory =
            Path.of(baseDirectory, "src", "main", "java", "com", "vnikolaev", "files")
            .toString();

    public static final String javaDirectory =
            Path.of(baseDirectory, "src", "main", "java")
                    .toString();

    public static final String testFilesDirectory =
            Path.of(baseDirectory, "src", "test", "java", "com", "vnikolaev", "datasource")
                    .toString();
}
