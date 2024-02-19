package uz.log.log_project;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Set;

@Component
public class LogJDBC {
    private Connection connection;
    private final LogRepository repository;

    public LogJDBC(ConfigData data, LogRepository repository) {
        this.repository = repository;
        try {
            this.connection = DriverManager.getConnection(data.getUrl(), data.getUsername(), data.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertToTables(Set<Log> list) {
        String sql = "INSERT INTO log (method_name, pan, client) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Log log : list) {
                statement.setString(1, log.getMethodName());
                statement.setString(2, log.getPan());
                statement.setString(3, log.getClient());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean isDuplicateCombination(String client, String pan) {
        String sql = "SELECT 1 FROM log WHERE client = ? AND pan = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client);
            ps.setString(2, pan);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
