package com.vnikolaev.requestdescriptors;

public class SaveAsCommandDescriptor extends RequestDescriptor {
    public SaveAsCommandDescriptor() {
        super("saveas", new String[] { "<newFile>" }, "saves all changes in the currently opened file in a new file specified by <newFile>");
    }
}
