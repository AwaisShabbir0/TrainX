package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.ResultSet;

public class UserProfileController {

    @FXML
    private TextField tfUsername, tfName, tfCNIC, tfPhone, tfAddress;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lblUsernameDisplay;

    private String currentUser;

    @FXML
    public void initialize() {
        currentUser = MainApp.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user logged in!");
            return;
        }

        tfUsername.setText(currentUser);
        lblUsernameDisplay.setText(currentUser);
        loadUserDetails();
    }

    private void loadUserDetails() {
        try {
            Conn c = Conn.getInstance();
            String query = "SELECT * FROM users WHERE username = '" + currentUser + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                // Table structure: 1:username, 2:password, 3:name, 4:cnic, 5:phone, 6:address
                tfName.setText(rs.getString(3)); // name
                tfCNIC.setText(rs.getString(4)); // cnic
                tfPhone.setText(rs.getString(5)); // phone
                tfAddress.setText(rs.getString(6)); // address
                pfPassword.setText(rs.getString(2)); // password
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load user details.");
        }
    }

    @FXML
    private void handleUpdate() {
        String name = tfName.getText();
        String cnic = tfCNIC.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = pfPassword.getText();

        if (name.isEmpty() || cnic.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all fields!");
            return;
        }

        if (!phone.matches("\\d+") || !cnic.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Phone and CNIC must be numeric!");
            return;
        }
        if (cnic.length() != 13) {
            showAlert(Alert.AlertType.WARNING, "Invalid CNIC", "CNIC must be 13 digits!");
            return;
        }

        try {
            Conn c = Conn.getInstance();
            
            // Check if CNIC exists for another user
            String checkQuery = "SELECT * FROM users WHERE cnic = '" + cnic + "' AND username != '" + currentUser + "'";
            ResultSet rs = c.s.executeQuery(checkQuery);
            if (rs.next()) {
                showAlert(Alert.AlertType.WARNING, "Duplicate CNIC", "This CNIC is already registered by another user.");
                return;
            }

            String query = "UPDATE users SET full_name = '" + name + "', cnic = '" + cnic + "', phone = '" + phone +
                    "', address = '" + address + "', password = '" + password + "' WHERE username = '" + currentUser
                    + "'";
            c.s.executeUpdate(query);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Updated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(currentUser, MainApp.getUserRole());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
