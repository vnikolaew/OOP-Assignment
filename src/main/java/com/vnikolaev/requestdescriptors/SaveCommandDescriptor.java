package com.vnikolaev.requestdescriptors;

public class SaveCommandDescriptor extends RequestDescriptor {
    public SaveCommandDescriptor() {
        super("save", new String[0], "saves all changes to the currently opened file");
    }
}
