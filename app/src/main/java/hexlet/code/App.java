package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;


public class App {
    private static final  int PORT = 7070;
    private static final  String LOCAL_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException, IOException {

        Map<String, String> env = System.getenv();

        Javalin app = getApp();

        app.start();
    }

    public static Javalin getApp() throws IOException, SQLException {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Logger started");

        Map<String, String> env = System.getenv();
        var hikariConfig = new HikariConfig();

        if (env.containsKey("JDBC_DATABASE_URL")) {
            var dbUrl = env.get("JDBC_DATABASE_URL");
            hikariConfig.setJdbcUrl(dbUrl);
        } else {
            hikariConfig.setJdbcUrl(LOCAL_URL);
        }

        var dataSource = new HikariDataSource(hikariConfig);

        var url = App.class.getClassLoader().getResource("urls.sql");
        var file = new File(url.getFile());
        var sql = Files.lines(file.toPath()).collect(Collectors.joining("\n"));

        try (var conn = dataSource.getConnection();
             var statement = conn.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;
        JavalinJte.init(createTemplateEngine());

        var app = Javalin.create(javalinConfig -> javalinConfig.plugins.enableDevLogging());

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::addUrl);
        app.get(NamedRoutes.urlsPath(), UrlsController::getUrls);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::showUrl);
        app.post(NamedRoutes.checksPath("{id}"), ChecksController::addCheck);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
