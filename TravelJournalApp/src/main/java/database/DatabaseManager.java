package database;

import database.DatabaseConnection;

public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseConnection connection;

    private DatabaseManager() {
        connection = new DatabaseConnection();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public DatabaseConnection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "DatabaseManager{" + "connection=" + connection + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseManager that = (DatabaseManager) o;
        return connection.equals(that.connection);
    }
}
