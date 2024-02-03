package hexlet.code;

import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;


public class App {

    private final static int PORT = 7070;
    private final static String localUrl = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws SQLException, IOException {

        Map<String, String> env = System.getenv();

        if (env.containsKey("JDBC_DATABASE_URL")) {
            System.out.println(env.get("JDBC_DATABASE_URL") + "THIS IS DB-URL");
            var dbUrl = env.get("JDBC_DATABASE_URL");
            getApp(dbUrl).start(5432);
        } else {
            System.out.println(localUrl);
            getApp(localUrl).start(7070);
        }
    }

    public static Javalin getApp(String dbUrl) throws IOException, SQLException {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Logger started");
        logger.info(dbUrl);

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbUrl);

        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResource("urls.sql");
        logger.info(String.valueOf(url));
        var file = new File(url.getFile());
        var sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));

        logger.info(sql);
        try (var conn = dataSource.getConnection();
             var statement = conn.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(javalinConfig -> {
            javalinConfig.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World!"));

        return app;
    }
}
