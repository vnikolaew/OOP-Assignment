package com.vnikolaev.requestdescriptors;

public class SearchQueryDescriptor extends RequestDescriptor {
    public SearchQueryDescriptor() {
        super("search", new String[] { "<searchterm>" }, "queries a given key in the currently opened file");
    }
}
