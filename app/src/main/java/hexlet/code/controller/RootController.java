package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {
    public static void index(Context ctx) {
        var message = ctx.sessionAttribute("message");
        if (message != null) {
            ctx.consumeSessionAttribute("message");
        }
        var page = new MainPage(message);
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
