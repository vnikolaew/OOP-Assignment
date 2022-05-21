package com.vnikolaev.abstractions;

import com.vnikolaev.requestdescriptors.RequestDescriptor;

public interface CommandDescriptionFormatter {
    String formatLine(RequestDescriptor descriptor);
}
