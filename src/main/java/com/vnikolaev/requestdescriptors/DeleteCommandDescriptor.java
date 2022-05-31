package com.vnikolaev.requestdescriptors;

public class DeleteCommandDescriptor extends RequestDescriptor {
    public DeleteCommandDescriptor() {
        super("delete", new String[] { "<path>" }, "deletes a given element if it exists in the currently opened file");
    }
}
