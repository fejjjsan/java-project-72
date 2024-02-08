package hexlet.code.repository;

import hexlet.code.model.Url;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static void save(Url entity) throws SQLException {
        var sql = "INSERT INTO urls (name, createdAt) VALUES (?, ?)";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCreatedAt());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB did not return an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id=?";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var result = preparedStatement.executeQuery();

            if (result.next()) {
                var name = result.getString("name");
                var createdAt = result.getString("createdAt");
                var url = new Url(name, createdAt);
                url.setId(id);

                return Optional.of(url);
            }
        }
        return Optional.empty();
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name=?";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            var result = preparedStatement.executeQuery();

            if (result.next()) {
                var id = result.getLong("id");
                var createdAt = result.getString("createdAt");
                var url = new Url(name, createdAt);
                url.setId(id);

                return Optional.of(url);
            }
        }
        return Optional.empty();
    }


    public static ArrayList<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";

        try (var conn = dataSource.getConnection();
                var preparedStatement = conn.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            var result = new ArrayList<Url>();

            while (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getString("createdAt");
                var id = resultSet.getLong("id");
                var url = new Url(name, createdAt);
                url.setId(id);
                result.add(url);
            }
            return result;
        }
    }

}
