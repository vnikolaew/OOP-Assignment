package com.vnikolaev.requestdescriptors;

public class UpdateCommandDescriptor extends RequestDescriptor {
    public UpdateCommandDescriptor() {
        super("set", new String[] { "<path>", "<value>" }, "updates a given key with the new value in the currently opened file");
    }
}
