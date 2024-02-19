package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.ChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Context;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

import hexlet.code.util.NamedRoutes;
import io.javalin.http.NotFoundResponse;
import org.apache.commons.validator.routines.UrlValidator;


public class UrlsController {

    public static void addUrl(Context ctx) throws URISyntaxException, SQLException {
        String url = ctx.formParam("url").trim();

        if (validateURL(url)) {
            String parsedURL = parseURL(url);
            if (UrlsRepository.findByName(parsedURL).isPresent()) {
                ctx.sessionAttribute("message", "сайт уже добавлен");
                ctx.redirect(NamedRoutes.urlsPath());
            } else if (validateURL(parsedURL)) {
                var newUrl = new Url(parsedURL);
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
        var checks = new HashMap<Long, UrlCheck>();
        if (!UrlsRepository.getEntities().isEmpty()) {
            for (var url : UrlsRepository.getEntities()) {
                var id = url.getId();
                if (ChecksRepository.getLastCheck(id).isPresent()) {
                    checks.put(id, ChecksRepository.getLastCheck(id).get());
                }
            }
        }

        var page = new UrlsPage(message, UrlsRepository.getEntities(), checks);
        ctx.render("urls/urls.jte", Collections.singletonMap("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var message = ctx.sessionAttribute("message");
        ctx.consumeSessionAttribute("message");
        var url = UrlsRepository.findByID(id)
                .orElseThrow(() -> new NotFoundResponse("URL not found"));
        var checks = ChecksRepository.getUrlChecks(id);
        var page = new UrlPage(message, url, checks);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }


    private static String parseURL(String url) throws URISyntaxException {
        var normalizedURL = url.replaceAll(" ", "");
        var uri = new URI(normalizedURL);
        return uri.getScheme() + "://" + uri.getAuthority();
    }

    private static Boolean validateURL(String url) {
        String[] schemas = {"http", "https"};
        var validator = new UrlValidator(schemas, UrlValidator.ALLOW_LOCAL_URLS);
        return validator.isValid(url);
    }

}
