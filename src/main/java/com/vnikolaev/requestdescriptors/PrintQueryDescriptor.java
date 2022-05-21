package com.vnikolaev.requestdescriptors;

public class PrintQueryDescriptor extends RequestDescriptor {
    public PrintQueryDescriptor() {
        super("print", new String[0], "prints the content of the currently opened file");
    }
}
