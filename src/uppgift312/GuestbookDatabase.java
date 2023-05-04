package uppgift312;

import java.sql.*;

public class GuestbookDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/guestbook";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    private Connection connection;

    public GuestbookDatabase() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void addEntry(GuestbookEntry entry) throws SQLException {
        String sql = "INSERT INTO entries (name, email, website, comment) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, entry.getName());
        statement.setString(2, entry.getEmail());
        statement.setString(3, entry.getWebsite());
        statement.setString(4, entry.getComment());
        statement.executeUpdate();
    }

    public String getEntries() throws SQLException {
        String sql = "SELECT * FROM entries ORDER BY created_at DESC";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        StringBuilder entries = new StringBuilder();
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            String email = result.getString("email");
            String website = result.getString("website");
            String comment = result.getString("comment");
            String created_at = result.getString("created_at");
            entries.append(name).append(" (").append(email).append(")").append(" wrote:\n");
            entries.append(comment).append("\n");
            entries.append("Posted on ").append(created_at).append("\n\n");
        }
        return entries.toString();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
