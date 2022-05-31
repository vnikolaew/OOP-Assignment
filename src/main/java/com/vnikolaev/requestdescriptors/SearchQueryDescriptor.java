package com.vnikolaev.requestdescriptors;

public class SearchQueryDescriptor extends RequestDescriptor {
    public SearchQueryDescriptor() {
        super("search", new String[] { "<searchterm>" }, "searches the JSON object for a specific path in the currently opened file");
    }
}
