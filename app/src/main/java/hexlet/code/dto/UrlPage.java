package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import java.util.List;
import lombok.Getter;


@Getter
public class UrlPage extends MainPage {
    private final Url url;
    private final List<UrlCheck> checks;

    public UrlPage(Object message, Url url, List<UrlCheck> checks) {
        super(message);
        this.url = url;
        this.checks = checks;
    }

}
