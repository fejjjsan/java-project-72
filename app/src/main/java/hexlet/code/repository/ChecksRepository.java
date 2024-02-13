package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ChecksRepository extends BaseRepository{
    public static void save(UrlCheck check) throws SQLException {
        var sql = """
                INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at)
                VALUES(?, ?, ?, ?, ?, ?)
                """;

        try (var conn = dataSource.getConnection();
                var prepareStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var ts = new Timestamp(new Date().getTime());
            prepareStatement.setLong(1, check.getUrlId());
            prepareStatement.setInt(2, check.getStatusCode());
            prepareStatement.setString(3, check.getTitle());
            prepareStatement.setString(4, check.getH1());
            prepareStatement.setString(5, check.getDescription());
            prepareStatement.setTimestamp(6, ts);
            prepareStatement.executeUpdate();

            var generatedKeys = prepareStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB did not return an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getUrlChecks(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id=?";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var urlCheck = UrlCheck.builder()
                        .id(resultSet.getLong("check_id"))
                        .urlId(urlId)
                        .statusCode(resultSet.getInt("status_code"))
                        .title(resultSet.getString("title"))
                        .h1(resultSet.getString("h1"))
                        .description(resultSet.getString("description"))
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .build();
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static Optional<UrlCheck> getLastCheck(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id=? ORDER BY created_at DESC LIMIT 1";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var urlCheck = UrlCheck.builder()
                        .urlId(resultSet.getLong("url_id"))
                        .statusCode(resultSet.getInt("status_code"))
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .build();
                return Optional.of(urlCheck);
            }
        }
        return Optional.empty();
    }

    public static ArrayList<UrlCheck> getEntities() throws SQLException {
        var sql = "SELECT * FROM url_checks";

        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var urlCheck = UrlCheck.builder()
                        .id(resultSet.getLong("check_id"))
                        .urlId(resultSet.getLong("url_id"))
                        .statusCode(resultSet.getInt("status_code"))
                        .title(resultSet.getString("title"))
                        .h1(resultSet.getString("h1"))
                        .description(resultSet.getString("description"))
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .build();
                result.add(urlCheck);
            }
            return result;
        }
    }

}
