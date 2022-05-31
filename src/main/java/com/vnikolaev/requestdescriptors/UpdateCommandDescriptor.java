package com.vnikolaev.requestdescriptors;

public class UpdateCommandDescriptor extends RequestDescriptor {
    public UpdateCommandDescriptor() {
        super("set", new String[] { "<path>", "<value>" }, "updates the specified element with a new value in the currently opened file");
    }
}
