package com.vnikolaev.requestdescriptors;

public class CloseCommandDescriptor extends RequestDescriptor {
    public CloseCommandDescriptor() {
        super("close", new String[] { "<file>" }, "opens a file");
    }
}
