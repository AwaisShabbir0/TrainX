package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMealController {

    @FXML
    private TextField tfPNR;
    @FXML
    private Label lblStatus, lblTotal;
    @FXML
    private GridPane menuGrid;
    @FXML
    private Button btnOrder;

    @FXML
    private CheckBox cbItem1, cbItem2, cbItem3, cbItem4;

    private int totalAmount = 0;

    @FXML
    public void initialize() {
        // Add listeners to update total
        cbItem1.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        cbItem2.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        cbItem3.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        cbItem4.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotal());

        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            Conn c = new Conn();
            String query = "CREATE TABLE IF NOT EXISTS catering (pnr VARCHAR(20), items VARCHAR(200), amount INT)";
            c.s.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotal() {
        totalAmount = 0;
        if (cbItem1.isSelected())
            totalAmount += 500;
        if (cbItem2.isSelected())
            totalAmount += 350;
        if (cbItem3.isSelected())
            totalAmount += 100;
        if (cbItem4.isSelected())
            totalAmount += 80;

        lblTotal.setText(String.valueOf(totalAmount));
    }

    @FXML
    private void handleCheckPNR() {
        String pnr = tfPNR.getText();
        if (pnr.isEmpty()) {
            lblStatus.setText("Please enter PNR");
            return;
        }

        try {
            Conn c = new Conn();
            // Verify PNR exists in reservation
            // Verify PNR exists in reservation AND belongs to current user
            String query = "SELECT * FROM reservation WHERE PNR = '" + pnr + "' AND account_username = '" + MainApp.getCurrentUser() + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                lblStatus.setText("PNR Verified! Select your meal.");
                lblStatus.setStyle("-fx-text-fill: green;");
                menuGrid.setDisable(false);
                btnOrder.setDisable(false);
            } else {
                if (pnr.length() == 13 && pnr.matches("\\d+")) {
                     lblStatus.setText("Enter PNR (PNR-XXX), not CNIC!");
                } else {
                    lblStatus.setText("Invalid PNR or Access Denied.");
                }
                lblStatus.setStyle("-fx-text-fill: red;");
                menuGrid.setDisable(true);
                btnOrder.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrder() {
        if (totalAmount == 0) {
            showAlert(Alert.AlertType.WARNING, "Empty Order", "Please select at least one item.");
            return;
        }

        String pnr = tfPNR.getText();
        StringBuilder items = new StringBuilder();
        if (cbItem1.isSelected())
            items.append("Biryani, ");
        if (cbItem2.isSelected())
            items.append("Sandwich, ");
        if (cbItem3.isSelected())
            items.append("Water, ");
        if (cbItem4.isSelected())
            items.append("Tea, ");

        // Remove trailing comma
        if (items.length() > 0)
            items.setLength(items.length() - 2);

        try {
            Conn c = new Conn();
            String currentUser = MainApp.getCurrentUser();

            // Check Balance
            String queryBal = "SELECT balance FROM wallet WHERE username = '" + currentUser + "'";
            ResultSet rsBal = c.s.executeQuery(queryBal);

            if (rsBal.next()) {
                double currentBal = rsBal.getDouble("balance");

                if (currentBal >= totalAmount) {
                    // Deduct Money
                    String deductQuery = "UPDATE wallet SET balance = balance - " + totalAmount + " WHERE username = '"
                            + currentUser + "'";
                    c.s.executeUpdate(deductQuery);

                    // Log Transaction
                    String historyQuery = "INSERT INTO wallet_history (username, amount, type, description) VALUES ('"
                            + currentUser + "', " + totalAmount + ", 'DEBIT', 'Meal Order for " + pnr + "')";
                    c.s.executeUpdate(historyQuery);

                    // Place Order
                    String query = "INSERT INTO catering VALUES('" + pnr + "', '" + items.toString() + "', "
                            + totalAmount + ")";
                    c.s.executeUpdate(query);

                    showAlert(Alert.AlertType.INFORMATION, "Order Placed",
                            "Meal booked successfully! Cost deduced from Wallet.");
                    handleBack();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Insufficient Funds",
                            "You do not have enough funds in your wallet. Cost: " + totalAmount + ", Balance: "
                                    + currentBal);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Wallet not found for current user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
