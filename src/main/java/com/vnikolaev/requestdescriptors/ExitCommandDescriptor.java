package com.vnikolaev.requestdescriptors;

public class ExitCommandDescriptor extends RequestDescriptor {
    public ExitCommandDescriptor() {
        super("exit", new String[0], "exits the program");
    }
}
