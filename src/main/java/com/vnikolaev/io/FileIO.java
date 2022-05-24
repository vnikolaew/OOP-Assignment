package com.vnikolaev.io;

import com.vnikolaev.abstractions.FileIODevice;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A simple class for interacting with the file system I/O.
 * Note that it's different from the base IODevice in that
 * it requires a file location as well.
 */
public class FileIO implements FileIODevice {

    @Override
    public void write(String filePath, String content) throws IOException {
        OutputStream stream = new FileOutputStream(filePath);
        byte[] data = content.getBytes(StandardCharsets.UTF_8);

        stream.write(data);
        stream.close();
    }

    @Override
    public String read(String filePath) throws IOException {
        InputStream stream = new FileInputStream(filePath);
        byte[] data = stream.readAllBytes();

        stream.close();
        return new String(data, StandardCharsets.UTF_8);
    }
}
