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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;


public class App {
    private static final  String LOCAL_DB = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException, IOException {

        Javalin app = getApp();

        app.start(getPort());
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
            hikariConfig.setJdbcUrl(LOCAL_DB);
        }
        var dataSource = new HikariDataSource(hikariConfig);

        var sql = readResourceFile("schema.sql");

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

    private static String readResourceFile(String fileName) throws IOException {
        var url = App.class.getClassLoader().getResource(fileName);
        var file = new File(url.getFile());
        try (var sql = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            return sql.collect(Collectors.joining("\n"));
        }
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
}
