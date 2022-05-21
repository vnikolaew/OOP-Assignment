package com.vnikolaev.requestdescriptors;

public class HelpCommandDescriptor extends RequestDescriptor {
    public HelpCommandDescriptor() {
        super("help", new String[0], "print this information");
    }
}
