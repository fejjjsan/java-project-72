package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@Setter
public class MainPage {
    private Object message;

    public final String formatTimestamp(Timestamp ts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ts.toLocalDateTime().format(formatter);
    }
}
