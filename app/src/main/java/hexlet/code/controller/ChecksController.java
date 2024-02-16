package hexlet.code.controller;


import hexlet.code.model.UrlCheck;
import hexlet.code.repository.ChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;

public class ChecksController {
    public static void addCheck(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        String urlName;

        if (UrlsRepository.findByID(urlId).isPresent()) {
            urlName = UrlsRepository.findByID(urlId).get().getName();
        } else {
            throw new NotFoundResponse();
        }

        try {
            var html = getHtml(urlName);
            var check = UrlCheck.builder()
                    .urlId(urlId)
                    .statusCode(getStatusCode(urlName))
                    .title(getTitle(html))
                    .h1(getH1(html))
                    .description(getDescription(html))
                    .build();
            ChecksRepository.save(check);

            ctx.sessionAttribute("message", "Страница успешно проверена");
            ctx.redirect(NamedRoutes.urlPath(urlId));

        } catch (UnirestException e) {
            ctx.sessionAttribute("message", "Некорректный адрес");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }

    }


    private static Document getHtml(String url) {
        String body = Unirest.get(url).asString().getBody();
        return Jsoup.parse(body);
    }

    private static String getTitle(Document html) {
        return html.title();
    }

    private static String getH1(Document html) {
        if (!html.select("h1").isEmpty()) {
            return html.selectFirst("h1").text();
        }
        return "";
    }

    private static String getDescription(Document html) {
        if (!html.select("meta").isEmpty()) {
            return html.selectFirst("meta[name=description]").attr("content");
        }
        return "";
    }

    private static int getStatusCode(String url) {
        return Unirest.get(url).asString().getStatus();
    }
}
