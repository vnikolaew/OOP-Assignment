package com.vnikolaev.datasource.pathinterpretors;

public interface JSONPathInterpreter {
    String[] getSegments(String jsonPath);
}
