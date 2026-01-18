package railwaymanagementsystem;

import java.sql.*;

public class DBInspector {
    public static void main(String[] args) {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM users LIMIT 1");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            System.out.println("--- USERS TABLE COLUMNS ---");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": " + rsmd.getColumnName(i));
            }
            System.out.println("---------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
