package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;

import java.sql.*;

public class UpdateDB {
    public static void main(String[] args) {
        try {
            Conn c = new Conn();

            // 1. Check/Create 'passenger' table
            try {
                // Check if table exists, if not create
                // Note: This is basic. Ideally use "CREATE TABLE IF NOT EXISTS"
                String q = "CREATE TABLE IF NOT EXISTS passenger (name varchar(50), nationality varchar(30), phone varchar(20), address varchar(100), cnic varchar(20), gender varchar(10), username varchar(50))";
                c.s.executeUpdate(q);
                
                // Alter table if exists (for migration)
                try {
                    c.s.executeUpdate("ALTER TABLE passenger ADD COLUMN username VARCHAR(50)");
                    System.out.println("Added 'username' column to 'passenger' table.");
                } catch (Exception e) {
                   // Column likely exists
                }
                
                System.out.println("Checked 'passenger' table.");
            } catch (Exception e) {
                System.out.println("Error checking 'passenger' table: " + e.getMessage());
            }

            // 2. Check/Create 'train' table
            try {
                // Ensure 5 columns matching AddTrainController
                String q = "CREATE TABLE IF NOT EXISTS train (train_code varchar(20), train_name varchar(50), source varchar(50), destination varchar(50), fair varchar(20))";
                c.s.executeUpdate(q);
                System.out.println("Checked 'train' table.");
            } catch (Exception e) {
                System.out.println("Error checking 'train' table: " + e.getMessage());
            }

            // 3. Update 'reservation' table columns
            String[] cols = {
                    "ALTER TABLE reservation ADD COLUMN tickets VARCHAR(5)",
                    "ALTER TABLE reservation ADD COLUMN class VARCHAR(20)",
                    "ALTER TABLE reservation ADD COLUMN seats VARCHAR(100)"
            };

            for (String query : cols) {
                try {
                    c.s.executeUpdate(query);
                    System.out.println("Executed: " + query);
                } catch (Exception e) {
                    // Ignore if column exists
                }
            }

            System.out.println("Database Update Complete.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
