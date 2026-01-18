package railwaymanagementsystem.admin;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.ResultSet;

public class AnalyticsController {

    @FXML
    private Label lblRevenue, lblBookings, lblPassengers, lblTrains;

    @FXML
    public void initialize() {
        loadAnalytics();
    }

    @FXML
    private void handleRefresh() {
        loadAnalytics();
    }

    private void loadAnalytics() {
        try {
            Conn c = new Conn();

            // 1. Total Revenue (Sum of all wallet debits related to ticket booking)
            // Note: Since we don't have a 'sales' table, we use wallet_history DEBITs.
            // Ideally should filter by description or type.
            String queryRevenue = "SELECT SUM(amount) as total FROM wallet_history WHERE type = 'DEBIT'";
            ResultSet rsRevenue = c.s.executeQuery(queryRevenue);
            if (rsRevenue.next()) {
                double rev = rsRevenue.getDouble("total");
                lblRevenue.setText("PKR " + String.format("%,.0f", rev));
            }

            // 2. Total Bookings
            // Reset Statement for new query or create new connection/statement ideally but simple approach:
            // Since executeQuery closes previous result set on same statement in some drivers/configs, best to use separate if unsure.
            // But usually safe if we finished reading. Let's create new statement just to be safe or re-use Conn if logic permits.
            // Simpler: Just new Conn() for each or re-use 'c' but carefully.
            // Let's reuse 'c' but get new statement for safety if driver allows. 
            // Actually, with standard JDBC, executing another query on same Statement closes previous ResultSet.
            // We already read rsRevenue, so it is fine.

            String queryBookings = "SELECT COUNT(*) as count FROM reservation";
            ResultSet rsBookings = c.s.executeQuery(queryBookings);
            if (rsBookings.next()) {
                lblBookings.setText(String.valueOf(rsBookings.getInt("count")));
            }

            String queryPassengers = "SELECT COUNT(*) as count FROM passenger";
            ResultSet rsPassengers = c.s.executeQuery(queryPassengers);
            if (rsPassengers.next()) {
                lblPassengers.setText(String.valueOf(rsPassengers.getInt("count")));
            }

            String queryTrains = "SELECT COUNT(*) as count FROM train";
            ResultSet rsTrains = c.s.executeQuery(queryTrains);
            if (rsTrains.next()) {
                lblTrains.setText(String.valueOf(rsTrains.getInt("count")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblRevenue.setText("Error");
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }
}
