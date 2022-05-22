package com.vnikolaev.abstractions;

import com.vnikolaev.requestdescriptors.RequestDescriptor;

public interface RequestDescriptionFormatter {
    String formatLine(RequestDescriptor descriptor);
}
