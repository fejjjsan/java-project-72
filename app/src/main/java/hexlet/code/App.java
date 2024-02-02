package hexlet.code;

import io.javalin.Javalin;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {

    private final static int PORT = 7070;
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Hello World");

        var app = getApp();
        app.start(PORT);
    }

    public static Javalin getApp() {
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World!"));

        return app;
    }
}
