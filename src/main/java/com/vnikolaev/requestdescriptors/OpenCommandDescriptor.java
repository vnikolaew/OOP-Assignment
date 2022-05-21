package com.vnikolaev.requestdescriptors;

public class OpenCommandDescriptor extends RequestDescriptor {
    public OpenCommandDescriptor() {
        super("open", new String[] { "<file>" }, "opens a file");
    }
}
