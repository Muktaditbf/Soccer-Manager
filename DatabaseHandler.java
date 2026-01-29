import java.sql.*;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:sports_club_v3.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS members (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "name TEXT, phone TEXT, email TEXT, " +
                         "market_value REAL, training_date TEXT, status TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS matches (id INTEGER PRIMARY KEY AUTOINCREMENT, team1 TEXT, team2 TEXT, score TEXT)");
            
            stmt.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('admin', 'pass')");
            System.out.println("Database v3 Checked.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}