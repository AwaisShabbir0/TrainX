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
            Conn c = Conn.getInstance();

            String queryRevenue = "SELECT SUM(amount) as total FROM wallet_history WHERE type = 'DEBIT'";
            ResultSet rsRevenue = c.s.executeQuery(queryRevenue);
            if (rsRevenue.next()) {
                double rev = rsRevenue.getDouble("total");
                lblRevenue.setText("PKR " + String.format("%,.0f", rev));
            }
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
