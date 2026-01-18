package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.util.Random;

public class CancelController {

    @FXML
    private TextField pnrField;
    @FXML
    private Label nameLabel, cancelNoLabel, trainCodeLabel, dateLabel;

    @FXML
    private void handleFetchDetails() {
        String pnr = pnrField.getText();
        if (pnr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter PNR");
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String currentUser = MainApp.getCurrentUser();
            String query = "select * from reservation where PNR = '" + pnr + "' AND account_username = '" + currentUser + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                trainCodeLabel.setText(rs.getString("train_code"));
                dateLabel.setText(rs.getString("ddate"));
                cancelNoLabel.setText("" + new Random().nextInt(1000000));
            } else {
                if (pnr.length() == 13 && pnr.matches("\\d+")) {
                     showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter the PNR (e.g. PNR-XXXX), not your CNIC.\nYou can find your PNR in the Journey History.");
                } else {
                     showAlert(Alert.AlertType.INFORMATION, "Not Found", "Ticket with this PNR not found for your account.");
                }
                clearLabels();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelTicket() {
        String pnr = pnrField.getText();
        String name = nameLabel.getText();
        String cancelno = cancelNoLabel.getText();
        String fcode = trainCodeLabel.getText();
        String date = dateLabel.getText();

        if (name.equals("---")) {
            showAlert(Alert.AlertType.WARNING, "Fetch First", "Please fetch ticket details first.");
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "insert into cancel values('" + pnr + "', '" + name + "', '" + cancelno + "', '" + fcode
                    + "', '" + date + "')";
            conn.s.executeUpdate(query);
            
            // Secure delete
            conn.s.executeUpdate("delete from reservation where PNR = '" + pnr + "' AND account_username = '" + MainApp.getCurrentUser() + "'");

            showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket Cancelled Successfully");
            MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    private void clearLabels() {
        nameLabel.setText("---");
        trainCodeLabel.setText("---");
        dateLabel.setText("---");
        cancelNoLabel.setText("---");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
