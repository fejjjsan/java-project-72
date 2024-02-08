package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Context;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.NotFoundResponse;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlsController {

    public static void addUrl(Context ctx) throws URISyntaxException, SQLException {
        String url = ctx.formParam("url").replaceAll(" ", "");


        if (validateURL(url)) {
            String parsedURL = parseURL(url);
            if (UrlsRepository.findByName(parsedURL).isPresent()) {
                ctx.sessionAttribute("message", "сайт уже добавлен");
                ctx.redirect(NamedRoutes.urlsPath());
            } else if (validateURL(parsedURL)) {
                var date = new Date();
                var timestamp = new Timestamp(date.getTime()).toString();
                var newUrl = new Url(parsedURL, timestamp);
                UrlsRepository.save(newUrl);

                ctx.sessionAttribute("message", "сайт успешно добавлен");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } else {
            ctx.sessionAttribute("message", "некорректный url");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void getUrls(Context ctx) throws SQLException {
        var message = ctx.sessionAttribute("message");
        ctx.consumeSessionAttribute("message");
        var page = new UrlsPage(UrlsRepository.getEntities(), message);
        ctx.render("urls/urls.jte", Collections.singletonMap("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Product not found"));

        var page = new UrlPage(url);

        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    private static String parseURL(String url) throws URISyntaxException {
        var normalizedURL = url.replaceAll(" ", "");
        var uri = new URI(normalizedURL);
        return uri.getScheme() +"://"+ uri.getAuthority();
    }

    private static Boolean validateURL(String url) {
        String[] schemas = {"http","https"};
        var validator = new UrlValidator(schemas);
        return validator.isValid(url);
    }

}