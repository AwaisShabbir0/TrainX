package railwaymanagementsystem;

import java.sql.*;

public class Conn {
    private static Connection c;
    public Statement s;
    
    private static Conn instance;

    // Singleton Pattern: Private Constructor
    private Conn() {
        try {
            if (c == null || c.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                c = DriverManager.getConnection("jdbc:mysql:///pakrailways", "root", "@waisshabbir0810");
            }
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Singleton Access Point
    public static synchronized Conn getInstance() {
        if (instance == null) {
            instance = new Conn();
        }
        return instance;
    }

    public Connection getConnection() {
        return c;
    }

    // Public method to execute SQL updates
    public void executeUpdate(String query) throws SQLException {
        s.executeUpdate(query);
    }
}
