package hexlet.code.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlCheck {
    private Long id;
    private Long urlId;
    private int statusCode;
    private String title;
    private String h1;
    private String description; // TEXT in postgres
    private Timestamp createdAt;

//    public UrlCheck() {}
}
