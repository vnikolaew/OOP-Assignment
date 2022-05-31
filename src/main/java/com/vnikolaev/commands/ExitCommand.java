package com.vnikolaev.commands;
import com.vnikolaev.abstractions.CLICommand;
import com.vnikolaev.results.CommandResult;

/**
 * A command for exiting the whole CLI application and terminating the process.
 * Note that any pending changes made to the JSON object won't be saved
 * and persisted.
 */
public class ExitCommand extends CLICommand {

    public ExitCommand() {
        super(new String[0]);
    }

    @Override
    protected int getRequiredArgumentsLength() {
        return 0;
    }

    @Override
    protected CommandResult executeCore() {
        exitGracefully();
        return CommandResult.success("Exiting the app ...");
    }
    
    private void exitGracefully() {
        System.out.println("Exiting app ... Goodbye!");
        pauseFor(2000);
        System.exit(0);
    }

    private void pauseFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

}
