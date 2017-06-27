package app;

import app.processors.BoardProcessor;
import org.neogroup.sparks.Application;
import org.neogroup.sparks.console.ConsoleModule;

public class Main {

    public static final long FIELD_MASK = 0xFF00;
    public static final int FIELD_OFFSET = Long.numberOfTrailingZeros(FIELD_MASK);

    public static void main(String[] args) {
        Application application = new Application();
        application.addModule(new ConsoleModule(application));
        application.registerProcessor(BoardProcessor.class);
        application.start();
    }
}
