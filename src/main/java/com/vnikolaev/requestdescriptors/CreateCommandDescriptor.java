package com.vnikolaev.requestdescriptors;

public class CreateCommandDescriptor extends RequestDescriptor {
    public CreateCommandDescriptor() {
        super("create", new String[] { "<path>", "<value>" }, "creates a new element with the given value in the currently opened file");
    }
}
