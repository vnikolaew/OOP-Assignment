package com.vnikolaev.datasource.pathinterpretors;

public class RouteBasedJSONPathInterpreter implements JSONPathInterpreter {

    @Override
    public String[] getSegments(String jsonPath) {
        String normalizedPath = normalizeString(jsonPath);
        if(normalizedPath.isEmpty()) {
            return new String[0];
        }

        return normalizedPath.split("/");
    }

    private String normalizeString(String path) {
        String newKey = removeWhiteSpaces(path);
        return newKey.startsWith("/")
                ? newKey.substring(1)
                : newKey;
    }

    private String removeWhiteSpaces(String path) {
        return path.replaceAll(" ", "");
    }
}
