package hr.fer.zemris.ecf.lab.engine.console;

/**
 * Created by Domagoj on 05/04/15.
 */
public class ConsoleFactory {

    public static Console createConsole() {
        if (DetectOS.isWindows()) {
            return new CommandPrompt();
        } else {
            return new Terminal();
        }
    }

}
