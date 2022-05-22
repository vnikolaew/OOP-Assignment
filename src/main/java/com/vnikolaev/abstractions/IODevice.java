package com.vnikolaev.abstractions;

import java.io.IOException;


/**
 * A main abstraction over the underlying I/O device that the
 * application is going to work with. Only supports simple reads
 * and writes.
 */
public interface IODevice {
    void write(String content) throws IOException;
    String read() throws IOException;
}
