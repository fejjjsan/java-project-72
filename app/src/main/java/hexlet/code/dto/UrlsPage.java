package hexlet.code.dto;

import hexlet.code.model.Url;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage {
    private List<Url> urls;
    private Object message;
}
