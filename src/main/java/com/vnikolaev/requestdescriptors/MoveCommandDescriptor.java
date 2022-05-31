package com.vnikolaev.requestdescriptors;

public class MoveCommandDescriptor extends RequestDescriptor {
    public MoveCommandDescriptor() {
        super("move", new String[] { "<from>", "<to>" }, "move one or more elements from <from> to <to>");
    }
}
