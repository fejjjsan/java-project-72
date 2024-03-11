package hexlet.code.dto;

import hexlet.code.model.Url;
import java.util.HashMap;
import java.util.List;
import hexlet.code.model.UrlCheck;
import lombok.Getter;

@Getter
public class UrlsPage extends MainPage {
    private final List<Url> urls;
    private final HashMap<Long, UrlCheck> checks;

    public UrlsPage(Object message, List<Url> urls, HashMap<Long, UrlCheck> checks) {
        super(message);
        this.urls = urls;
        this.checks = checks;
    }
}
