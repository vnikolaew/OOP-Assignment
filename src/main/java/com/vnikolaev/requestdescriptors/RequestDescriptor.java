package com.vnikolaev.requestdescriptors;

public abstract class RequestDescriptor {

    protected final String commandName;
    protected final String[] args;
    protected final String commandInfo;

    protected RequestDescriptor(String commandName, String[] args, String commandInfo) {
        this.commandName = commandName;
        this.args = args;
        this.commandInfo = commandInfo;
    }

    public String getCommandInfo() {
        return commandInfo;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}
