package hexlet.code.dto;

import hexlet.code.model.Url;

import java.util.HashMap;
import java.util.List;

import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage {
    private List<Url> urls;
    private HashMap<Long, UrlCheck> checks;
    private Object message;
}
