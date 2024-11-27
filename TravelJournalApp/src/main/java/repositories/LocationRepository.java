package repositories;

import models.exceptions.InvalidLocationException;
import models.Location;
import java.sql.*;

public class LocationRepository {
    private Connection connection;

    public LocationRepository(Connection connection) {
        this.connection = connection;
    }

    public Location findLocationById(int id) throws SQLException {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Location(
                            resultSet.getInt("id"),
                            resultSet.getString("country"),
                            resultSet.getString("city"),
                            resultSet.getInt("countryId")
                    );
                }
            }
        }
        return null;
    }

    public int getLocationId(String country, String city) throws SQLException {
        String query = "SELECT id FROM locations WHERE country = ? AND city = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, country);
            statement.setString(2, city);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return addLocation(country, city);
            }
        }
    }

    private int addLocation(String country, String city) throws SQLException {
        String sql = "INSERT INTO locations (country, city) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, country);
            statement.setString(2, city);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to add location, no ID obtained.");
            }
        }
    }

}