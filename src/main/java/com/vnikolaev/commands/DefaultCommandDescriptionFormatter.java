package com.vnikolaev.commands;

import com.vnikolaev.abstractions.CommandDescriptionFormatter;
import com.vnikolaev.requestdescriptors.RequestDescriptor;

public class DefaultCommandDescriptionFormatter
        implements CommandDescriptionFormatter {

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
