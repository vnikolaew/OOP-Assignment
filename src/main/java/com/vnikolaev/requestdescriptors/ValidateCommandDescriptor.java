package com.vnikolaev.requestdescriptors;

public class ValidateCommandDescriptor extends RequestDescriptor {
    public ValidateCommandDescriptor() {
        super("validate", new String[0], "validates the JSON schema in the currently opened file");
    }
}
