package app;

import app.processors.MatchConsoleProcessor;
import org.neogroup.sparks.Application;
import org.neogroup.sparks.console.ConsoleModule;

public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        application.addModule(new ConsoleModule(application));
        application.registerProcessor(MatchConsoleProcessor.class);
        application.start();
    }
}
