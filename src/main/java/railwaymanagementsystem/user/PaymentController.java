package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

public class PaymentController {

    @FXML
    private Label amountLabel, pnrLabel, seatsLabel, lblWalletBalance, lblError;
    @FXML
    private Button btnPay;

    private String pnr, username, cnic, name, nationality, trainName, trainCode, src, des, date, sClass;
    private double amount;
    private double currentBalance = 0;
    private List<String> seats;

    public void setBookingDetails(String pnr, double amount, List<String> seats, String username, String cnic,
            String name, String nationality, String trainName, String trainCode,
            String src, String des, String date, String sClass) {
        this.pnr = pnr;
        this.amount = amount;
        this.seats = seats;
        this.username = username;
        this.cnic = cnic;
        this.name = name;
        this.nationality = nationality;
        this.trainName = trainName;
        this.trainCode = trainCode;
        this.src = src;
        this.des = des;
        this.date = date;
        this.sClass = sClass;

        amountLabel.setText("PKR " + amount);
        pnrLabel.setText(pnr);
        seatsLabel.setText(String.join(", ", seats));

        checkWalletBalance();
    }

    private void checkWalletBalance() {
        try {
            Conn c = Conn.getInstance();
            String query = "SELECT balance FROM wallet WHERE username = '" + MainApp.getCurrentUser() + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
                lblWalletBalance.setText("PKR " + String.format("%.2f", currentBalance));

                if (currentBalance < amount) {
                    lblError.setText("Insufficient Balance! Please recharge your wallet.");
                    btnPay.setDisable(true);
                } else {
                    lblError.setText("");
                    btnPay.setDisable(false);
                }
            } else {
                lblWalletBalance.setText("PKR 0.00");
                btnPay.setDisable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePay() {
        // Double check balance just in case
        if (currentBalance < amount) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Funds", "You do not have enough balance.");
            return;
        }

        try {
            Conn conn = Conn.getInstance();

            // 1. Deduct Money
            String deductQuery = "UPDATE wallet SET balance = balance - " + amount + " WHERE username = '"
                    + MainApp.getCurrentUser() + "'";
            conn.s.executeUpdate(deductQuery);

            // 2. Log Transaction
            String historyQuery = "INSERT INTO wallet_history (username, amount, type, description) VALUES ('"
                    + MainApp.getCurrentUser() + "', " + amount + ", 'DEBIT', 'Ticket Booking " + pnr + "')";
            conn.s.executeUpdate(historyQuery);

            // 3. Create Reservation
            String ticketId = "TIC-" + new Random().nextInt(10000);
            String seatString = String.join(", ", seats);

            String query = "insert into reservation values('" + pnr + "', '" + ticketId + "', '"
                    + username + "', '" + cnic + "', '" + name + "', '" + nationality + "', '" + trainName + "', '"
                    + trainCode + "', '" + src + "', '" + des + "', '" + date + "', '" + seats.size() + "', '" + sClass
                    + "', '" + seatString + "')";
            conn.s.executeUpdate(query);

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Payment Successful from Wallet!\nRemaining Balance: PKR " + (currentBalance - amount));

            MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Transaction Failed", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        MainApp.showBookTicket(); // Go back to booking
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
