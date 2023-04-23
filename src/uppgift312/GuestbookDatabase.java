package uppgift312;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestbookDatabase {
    private Connection conn;

    public GuestbookDatabase(String url, String user, String password) throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
        createTable();
    }

    private void createTable() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS guestbook_entries (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), email VARCHAR(255), website VARCHAR(255), comment TEXT, PRIMARY KEY (id))";
            stmt.executeUpdate(sql);
        }
    }

    public List<Guestbook> getAllEntries() throws SQLException {
        List<Guestbook> entries = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM guestbook_entries ORDER BY id DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Guestbook entry = new Guestbook(rs.getString("name"), rs.getString("email"), rs.getString("website"), rs.getString("comment"));
                entries.add(entry);
            }
        }
        return entries;
    }

    public void addEntry(Guestbook entry) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO guestbook_entries (name, email, website, comment) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, entry.getName());
            stmt.setString(2, entry.getEmail());
            stmt.setString(3, entry.getWebsite());
            stmt.setString(4, entry.getComment());
            stmt.executeUpdate();
        }
    }

    public void updateEntry(int id, Guestbook entry) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE guestbook_entries SET name=?, email=?, website=?, comment=? WHERE id=?")) {
            stmt.setString(1, entry.getName());
            stmt.setString(2, entry.getEmail());
            stmt.setString(3, entry.getWebsite());
            stmt.setString(4, entry.getComment());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }

    public void deleteEntry(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM guestbook_entries WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        conn.close();
    }
}

