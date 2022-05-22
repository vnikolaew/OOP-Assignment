package com.vnikolaev.commands;

import com.vnikolaev.abstractions.RequestDescriptionFormatter;
import com.vnikolaev.requestdescriptors.RequestDescriptor;

/**
 * A helper String formatting class responsible for displaying
 * a nice description / information about an application's request.
 */
public class DefaultRequestDescriptionFormatter
        implements RequestDescriptionFormatter {

    private static final String newLine = System.lineSeparator();
    private static final char whiteSpace = ' ';
    private static final int space = 30;

    @Override
    public String formatLine(RequestDescriptor descriptor) {
        StringBuilder info = new StringBuilder();
        info.append(descriptor.getCommandName());

        for(String arg : descriptor.getArgs()) {
            info.append(whiteSpace).append(arg);
        }

        padStringToLeft(info);

        info.append(whiteSpace)
                .append(descriptor.getCommandInfo())
                .append(newLine);

        return info.toString();
    }

    private void padStringToLeft(StringBuilder sb) {
        while(sb.length() < space) {
            sb.append(whiteSpace);
        }
    }
}
