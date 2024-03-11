package hexlet.code.controller;


import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.ChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;

public class ChecksController {
    public static void addCheck(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlsRepository.findByID(urlId)
                .orElseThrow(NotFoundResponse::new);
        String urlName = url.getName();

        try {
            var response = Unirest.get(urlName).asString();
            String body = response.getBody();
            var statusCode = response.getStatus();
            var html = Jsoup.parse(body);
            var check = UrlCheck.builder().urlId(urlId)
                    .statusCode(statusCode)
                    .title(getTitle(html))
                    .h1(getH1(html))
                    .description(getDescription(html))
                    .build();
            ChecksRepository.save(check);
            ctx.sessionAttribute("message", "Страница успешно проверена");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (Exception e) {
            ctx.sessionAttribute("message", "Некорректный адрес");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
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
            if (html.select("meta[name=description]").hasAttr("content")) {
                return html.selectFirst("meta[name=description]").attr("content");
            }
        }
        return "";
    }
}
