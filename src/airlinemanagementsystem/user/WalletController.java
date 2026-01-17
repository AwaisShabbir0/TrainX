package airlinemanagementsystem.user;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WalletController {

    @FXML
    private Label lblBalance;
    @FXML
    private TextField tfAmount, tfCardNumber, tfExpiry, tfCVV;
    @FXML
    private TableView<Transaction> historyTable;
    @FXML
    private TableColumn<Transaction, String> colDate, colDesc, colAmount, colType;

    private String currentUser;
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        currentUser = MainApp.getCurrentUser();

        // Setup table columns
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colAmount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        historyTable.setItems(transactions);

        initDB();
        loadWalletData();
    }

    private void initDB() {
        try {
            Conn c = new Conn();
            // Create Wallet Table
            String sql1 = "CREATE TABLE IF NOT EXISTS wallet (username VARCHAR(50) PRIMARY KEY, balance DECIMAL(10,2))";
            c.s.executeUpdate(sql1);

            // Create Transaction History Table
            String sql2 = "CREATE TABLE IF NOT EXISTS wallet_history (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50), amount DECIMAL(10,2), type VARCHAR(20), description VARCHAR(100), tdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            c.s.executeUpdate(sql2);

            // Create user entry if not exists
            String checkUser = "SELECT * FROM wallet WHERE username = '" + currentUser + "'";
            ResultSet rs = c.s.executeQuery(checkUser);
            if (!rs.next()) {
                String insert = "INSERT INTO wallet VALUES ('" + currentUser + "', 0.00)";
                c.s.executeUpdate(insert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWalletData() {
        try {
            Conn c = new Conn();

            // Load Balance
            String queryBal = "SELECT balance FROM wallet WHERE username = '" + currentUser + "'";
            ResultSet rsBal = c.s.executeQuery(queryBal);
            if (rsBal.next()) {
                double bal = rsBal.getDouble("balance");
                lblBalance.setText("Rs. " + String.format("%.2f", bal));
            }

            // Load History
            transactions.clear();
            String queryHist = "SELECT * FROM wallet_history WHERE username = '" + currentUser
                    + "' ORDER BY tdate DESC";
            ResultSet rsHist = c.s.executeQuery(queryHist);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            while (rsHist.next()) {
                String date = sdf.format(rsHist.getTimestamp("tdate"));
                String desc = rsHist.getString("description");
                String amt = String.format("%.2f", rsHist.getDouble("amount"));
                String type = rsHist.getString("type");

                transactions.add(new Transaction(date, desc, amt, type));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddMoney() {
        String input = tfAmount.getText();
        String card = tfCardNumber.getText();
        String exp = tfExpiry.getText();
        String cvv = tfCVV.getText();

        if (card.isEmpty() || exp.isEmpty() || cvv.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Details", "Please enter valid card details.");
            return;
        }

        if (input.isEmpty() || !input.matches("\\d+(\\.\\d+)?")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Amount", "Please enter a valid numeric amount.");
            return;
        }

        double amount = Double.parseDouble(input);
        if (amount <= 0) {
            showAlert(Alert.AlertType.WARNING, "Invalid Amount", "Amount must be greater than zero.");
            return;
        }

        try {
            Conn c = new Conn();

            // Update Balance
            String update = "UPDATE wallet SET balance = balance + " + amount + " WHERE username = '" + currentUser
                    + "'";
            c.s.executeUpdate(update);

            // Add History Record
            String history = "INSERT INTO wallet_history (username, amount, type, description) VALUES ('"
                    + currentUser + "', " + amount + ", 'CREDIT', 'Added Funds via Card "
                    + card.substring(Math.max(0, card.length() - 4)) + "')";
            c.s.executeUpdate(history);

            tfAmount.clear();
            tfCardNumber.clear();
            tfExpiry.clear();
            tfCVV.clear();
            loadWalletData(); // Refresh UI
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rs. " + amount + " added to your wallet successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
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

    // Inner class for TableView
    public static class Transaction {
        private final String date;
        private final String description;
        private final String amount;
        private final String type;

        public Transaction(String date, String description, String amount, String type) {
            this.date = date;
            this.description = description;
            this.amount = amount;
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public String getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }
    }
}
