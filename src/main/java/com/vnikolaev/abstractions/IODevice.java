package com.vnikolaev.abstractions;

import java.io.IOException;

public interface IODevice {
    void write(String content) throws IOException;
    String read() throws IOException;
}
