package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.dto.MainPage;
import hexlet.code.model.Url;
import hexlet.code.repository.ChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.stream.Collectors;

public final class AppTest {

    private Javalin app;
    private static final MockWebServer MOCK_WEB_SERVER = new MockWebServer();

    private static String getResourcePage(String fileName) throws IOException {
        var fileLoc = AppTest.class.getClassLoader().getResource(fileName);
        var file = new File(fileLoc.getFile());
        return Files.lines(file.toPath()).collect(Collectors.joining("\n"));
    }

    @BeforeAll
    public static void startMock() throws IOException {
        var response = new MockResponse()
                .addHeader("Content-Type", "text/html")
                .setResponseCode(200)
                .setBody(getResourcePage("index.html"));

        MOCK_WEB_SERVER.enqueue(response);
        MOCK_WEB_SERVER.enqueue(response);
        MOCK_WEB_SERVER.start();
    }

    @AfterAll
    public static void shutdownMock() throws IOException {
        MOCK_WEB_SERVER.shutdown();
    }

    @BeforeEach
    public void setApp() throws SQLException, IOException {
        String localUrl = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";
        app = App.getApp();
    }


    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testUrlCreation() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://hexlet.io/";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://hexlet.io/");
            UrlsRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(1L));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        });
    }


    @Test
    public void testUrlCheck() {
        JavalinTest.test(app, (server, client) -> {

            String baseUrl = MOCK_WEB_SERVER.url("/").toString();
            var responseBody = "url=" + baseUrl;

            var response1 = client.post(NamedRoutes.urlsPath(), responseBody);
            assertThat(response1.code()).isEqualTo(200);
            var name = UrlsRepository.findByID(1L).get().getName();
            assertThat(response1.body().string()).contains(name);

            var response2 = client.post(NamedRoutes.checksPath(1L));
            assertThat(response2.code()).isEqualTo(200);
            var body = response2.body().string();
            assertThat(body).contains("I'm title");
            assertThat(body).contains("Хекслет — Лучшая школа программирования");
            assertThat(body).contains("Hello Hexlet!");
            assertThat(body).contains("200");
            });
    }


    @Test
    public void testUrlsLastChecks() {
        JavalinTest.test(app, (server, client) -> {
            String baseUrl = MOCK_WEB_SERVER.url("/").toString();
            var responseBody = "url=" + baseUrl;
            client.post(NamedRoutes.urlsPath(), responseBody);
            client.post(NamedRoutes.checksPath(1L));

            var response = client.get(NamedRoutes.urlsPath());
            var body = response.body().string();
            var createdAt = MainPage.formatTimestamp(ChecksRepository.getLastCheck(1L).get().getCreatedAt());
            assertThat(body).contains("<td>" + createdAt + "</td>");
        });
    }

}
