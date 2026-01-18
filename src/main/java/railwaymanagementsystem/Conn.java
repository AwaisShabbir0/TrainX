package railwaymanagementsystem;

import java.sql.*;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///pakrailways", "root", "@waisshabbir0810");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Public method to execute SQL updates
    public void executeUpdate(String query) throws SQLException {
        s.executeUpdate(query);
    }
}
