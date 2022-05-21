package com.vnikolaev.io;

import com.vnikolaev.abstractions.IODevice;

import java.util.Scanner;

public class ConsoleIO implements IODevice {

    @Override
    public void write(String content) {
        System.out.print(content);
    }

    @Override
    public String read() {
        return new Scanner(System.in).nextLine();
    }
}
