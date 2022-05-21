package com.vnikolaev.requestdescriptors;

public class SaveAsCommandDescriptor extends RequestDescriptor {
    public SaveAsCommandDescriptor() {
        super("saveas", new String[] { "<file>" }, "saves the currently opened file in <file>");
    }
}
