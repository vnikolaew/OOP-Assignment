package com.vnikolaev.requestdescriptors;

public class CloseCommandDescriptor extends RequestDescriptor {
    public CloseCommandDescriptor() {
        super("close", new String[] { "<file>" }, "closes the currently opened file without saving any changes");
    }
}
