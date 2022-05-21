package com.vnikolaev.requestdescriptors;

public class SaveCommandDescriptor extends RequestDescriptor {
    public SaveCommandDescriptor() {
        super("save", new String[0], "saves the currently opened file");
    }
}
