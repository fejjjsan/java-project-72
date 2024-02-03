package hexlet.code.model;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private String createdAt;
}
