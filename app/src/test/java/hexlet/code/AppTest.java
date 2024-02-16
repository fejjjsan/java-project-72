package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public final class AppTest {

    private Javalin app;

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
    public void testUrlPost() {
        JavalinTest.test(app, (((server, client) -> {
            var requestBody = "url=https://hexlet.io/";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        })));
    }








//    @Test
//    public void testCheck() {
//        MockWebServer server = new MockWebServer();
//
//        server.enqueue(new MockResponse().setBody(""));
//    }

}
