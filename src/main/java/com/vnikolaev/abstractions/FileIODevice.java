package com.vnikolaev.abstractions;

import java.io.IOException;


/**
 * A main abstraction over the underlying file I/O device that the
 * application is going to work with. Only supports simple reads
 * and writes, but note that it also requires specifying file path.
 */
public interface FileIODevice {
    void write(String filePath, String content) throws IOException;
    String read(String filePath) throws IOException;
}
