package com.vnikolaev.abstractions;

/**
 * A simple interface to hide the implementation logic for parsing / interpreting
 * an input JSON path into smaller segments / parts.
 */
public interface JSONPathInterpreter {
    String[] getSegments(String jsonPath);
}
