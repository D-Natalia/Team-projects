package repositories;

import database.DatabaseConnection;
import models.JournalEntry;
import models.Location;
import models.exceptions.EntryNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JournalEntryRepository {
    private Connection connection;

    public JournalEntryRepository(Connection connection) {
        this.connection = connection;
    }

    public void addEntry(JournalEntry entry) {
        String sql = "INSERT INTO journal_entries (title, content, location_id, cost, is_public, image_path, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entry.getTitle());
            statement.setString(2, entry.getContent());
            statement.setInt(3, entry.getLocation().getId());
            statement.setDouble(4, entry.getCost());
            statement.setBoolean(5, entry.isPublic());
            statement.setString(6, entry.getImagePath());
            statement.setInt(7, entry.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<JournalEntry> getExperiencesByCountry(String countryName) {
        List<JournalEntry> journalEntries = new ArrayList<>();
        String query = "SELECT je.id, je.title, je.content, je.cost, je.is_public, je.image_path, loc.id AS location_id, loc.country, loc.city " +
                "FROM journal_entries je " +
                "JOIN locations loc ON je.location_id = loc.id " +
                "JOIN countries c ON loc.countryId = c.id " +
                "WHERE c.name = ? AND je.is_public = 1";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, countryName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    double cost = rs.getDouble("cost");
                    boolean isPublic = rs.getBoolean("is_public");
                    String imagePath = rs.getString("image_path");

                    int locationId = rs.getInt("location_id");
                    String country = rs.getString("country");
                    String city = rs.getString("city");
                    Location location = new Location(locationId, country, city, 0);


                    JournalEntry entry = new JournalEntry(
                            id,
                            title,
                            content,
                            location,
                            isPublic,
                            cost,
                            imagePath,
                            0
                    );

                    journalEntries.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return journalEntries;
    }

    public List<JournalEntry> getAllEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        String sql = "SELECT je.*, l.country, l.city, l.countryId FROM journal_entries je JOIN locations l ON je.location_id = l.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                entries.add(buildJournalEntry(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public JournalEntry findEntryByTitle(String title) throws EntryNotFoundException {
        String query = "SELECT je.*, l.country, l.city, l.countryId FROM journal_entries je JOIN locations l ON je.location_id = l.id WHERE je.title = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildJournalEntry(resultSet);
            } else {
                throw new EntryNotFoundException("Journal entry not found for title: " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntryNotFoundException("Error retrieving entry with title: " + title);
        }
    }

    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM countries";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                countries.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public void removeEntry(JournalEntry entry) {
        String sql = "DELETE FROM journal_entries WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entry.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<JournalEntry> getEntriesByUserId(int userId) {
        List<JournalEntry> entries = new ArrayList<>();
        String sql = "SELECT je.*, l.country, l.city, l.countryId FROM journal_entries je JOIN locations l ON je.location_id = l.id WHERE je.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entries.add(buildJournalEntry(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    private JournalEntry buildJournalEntry(ResultSet resultSet) throws SQLException {
        Location location = new Location(
                resultSet.getInt("location_id"),
                resultSet.getString("country"),
                resultSet.getString("city"),
                resultSet.getInt("countryId")
        );
        return new JournalEntry(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                location,
                resultSet.getBoolean("is_public"),
                resultSet.getDouble("cost"),
                resultSet.getString("image_path"),
                resultSet.getInt("user_id")
        );
    }
}
