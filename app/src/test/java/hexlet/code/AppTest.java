package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
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

import java.io.IOException;
import java.sql.SQLException;

public final class AppTest {

    private Javalin app;
    private static MockWebServer mock;


    @BeforeAll
    public static void startMock() throws IOException {
        mock = new MockWebServer();
        mock.start();
    }

    @AfterAll
    public static void shutdownMock() throws IOException {
        mock.shutdown();
    }

    @BeforeEach
    public void setApp() throws SQLException, IOException {
        String localUrl = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";
        app = App.getApp(localUrl);
    }


    @Test
    public void testMainPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (((server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        })));
    }

    @Test
    public void testUrlCreation() {
        JavalinTest.test(app, (((server, client) -> {
            var requestBody = "url=https://hexlet.io/";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        })));
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (((server, client) -> {
            var url = new Url("https://hexlet.io/");
            UrlsRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(1L));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        })));
    }


    @Test
    public void testUrlCheck() {

            JavalinTest.test(app, (((server, client) -> {

                var response = new MockResponse()
                        .addHeader("Content-Type", "text/html")
                        .setBody("""
                    {<!DOCTYPE html>
                    <html lang="en">
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <meta property="description" content="I'm description">
                      <title>Document</title>
                    </head>
                    <body>
                      <h1>Hexlet</h1>
                    </body>
                    </html>}
                    """);

                mock.enqueue(response);
                String baseUrl = mock.url("/").toString();
                var responseBody = "url=" + baseUrl;
                var url = new Url(baseUrl);
                UrlsRepository.save(url);
                var response1 = client.post(NamedRoutes.urlsPath(), responseBody);
                assertThat(response1.code()).isEqualTo(200);
                assertThat(response1.body().string()).contains(baseUrl);
                var response2 = client.post(NamedRoutes.checksPath(1L));
                assertThat(response2.code()).isEqualTo(200);
                assertThat(response2.body().string()).contains("Document");
            })));
    }

}
