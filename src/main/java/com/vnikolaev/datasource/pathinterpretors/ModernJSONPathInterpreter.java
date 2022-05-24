package com.vnikolaev.datasource.pathinterpretors;

import java.util.ArrayList;
import java.util.List;

public class ModernJSONPathInterpreter implements JSONPathInterpreter {

    private static final char rootPath = '$';
    private static final char openingBracket = '[';
    private static final char closingBracket = ']';
    private static final char dotCharacter = '.';
    private static final char singleQuote = '\'';

    @Override
    public String[] getSegments(String jsonPath) {

        if(jsonPath.length() == 1 && jsonPath.charAt(0) == rootPath) {
            return new String[0];
        }

        List<String> segments = new ArrayList<>();
        char[] chars = jsonPath.toCharArray();
        int currentChar = 0;

        if(chars[currentChar] == rootPath) currentChar++;
        while(currentChar < chars.length) {
            currentChar = parseJsonSegment(currentChar, chars, segments);
        }

        return segments.toArray(new String[0]);
    }

    private int parseJsonSegment(
            int currentChar, char[] chars, List<String> segments) {

        StringBuilder builder = new StringBuilder();
        if(chars[currentChar] == dotCharacter) {
            currentChar++;

            while(currentChar < chars.length
                    && (chars[currentChar] != dotCharacter
                    && chars[currentChar] != openingBracket)) {
                builder.append(chars[currentChar++]);
            }

            segments.add(builder.toString());
        } else if(chars[currentChar] == openingBracket) {
            if(chars[++currentChar] == '\'') {
                currentChar = parseJsonPropertyInsideQuotes(++currentChar, chars, segments);
            } else {
                currentChar = parseJsonIndexInsideBrackets(currentChar, chars, segments);
            }
        }

        return currentChar;
    }

    private int parseJsonIndexInsideBrackets(
            int currentChar, char[] chars, List<String> segments) {

        StringBuilder builder = new StringBuilder();
        while(currentChar < chars.length && chars[currentChar] != closingBracket) {
            builder.append(chars[currentChar++]);
        }
        if(currentChar == chars.length) {
            // TODO: Json path is wrong! Throw an exception!
            // It's impossible that we've reached the end of the path
            // since we should now be at the position of the closing
            // bracket which is clearly < chars.length
            return currentChar;
        }
        try {
            Integer index = Integer.parseInt(builder.toString());
        } catch (NumberFormatException e) {
            // TODO: Json path is wrong! Throw an exception!
            // Number index inside brackets is invalid.
            return ++currentChar;
        }

        segments.add(builder.toString());
        return ++currentChar;
    }

    private int parseJsonPropertyInsideQuotes(
            int currentChar, char[] chars, List<String> segments) {
        StringBuilder builder = new StringBuilder();
        while(currentChar < chars.length && chars[currentChar] != singleQuote) {
            builder.append(chars[currentChar++]);
        }
        if(currentChar == chars.length || chars[++currentChar] != closingBracket) {
            // TODO: Json path is wrong! Throw an exception!
            // Either we have a missing closing bracket or we've reached
            // the end of the path without closing it...
            return currentChar;
        }
        segments.add(builder.toString());
        return ++currentChar;
    }
}
