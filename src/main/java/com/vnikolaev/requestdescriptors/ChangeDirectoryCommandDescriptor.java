package com.vnikolaev.requestdescriptors;

public class ChangeDirectoryCommandDescriptor extends RequestDescriptor {
    public ChangeDirectoryCommandDescriptor() {
        super("cd", new String[] {"<directory>"}, "change the current working directory using either a relative or an absolute path");
    }
}
