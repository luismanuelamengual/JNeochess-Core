package example;

import example.processors.BoardProcessor;
import org.neogroup.httpserver.HttpRequest;
import org.neogroup.httpserver.HttpResponse;
import org.neogroup.sparks.Application;
import org.neogroup.sparks.console.ConsoleModule;
import org.neogroup.sparks.web.routing.Route;

import javax.xml.ws.Response;
import java.util.function.Function;

import sparks.*;

import static sparks.Spark.get;

public class Main {

    public static void main(String[] args) {

        /*Application application = new Application();
        ConsoleModule module = new ConsoleModule(application);
        module.registerProcessor(BoardProcessor.class);

        application.addModule(module);
        application.start();*/

        get("Rama", "titoch");

    }
}
