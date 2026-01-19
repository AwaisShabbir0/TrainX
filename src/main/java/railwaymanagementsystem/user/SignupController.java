package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import railwaymanagementsystem.utils.ValidationUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.ResultSet;

public class SignupController {

    @FXML
    private TextField usernameField, nameField, cnicField, phoneField, addressField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String cnic = cnicField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || cnic.isEmpty() || phone.isEmpty()
                || address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all fields!");
            return;
        }
        if (!ValidationUtils.isValidCNIC(cnic)) {
            showAlert(Alert.AlertType.WARNING, "Invalid CNIC", "CNIC must be exactly 13 digits (numeric)!");
            return;
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone", "Phone number must be exactly 11 digits (numeric)!");
            return;
        }

        try {
            Conn c = Conn.getInstance();
            ResultSet rs = c.s.executeQuery("select * from users where username = '" + username + "'");
            if (rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Duplicate", "Username already exists!");
                return;
            }
            
            ResultSet rsCNIC = c.s.executeQuery("select * from users where cnic = '" + cnic + "'");
            if (rsCNIC.next()) {
                showAlert(Alert.AlertType.ERROR, "Duplicate", "An account with this CNIC already exists!");
                return;
            }

            String query = "INSERT INTO users VALUES('" + username + "', '" + password + "', '" + name + "', '"
                    + cnic + "', '" + phone + "', '" + address + "')";
            c.s.executeUpdate(query);

            String passengerQuery = "INSERT INTO passenger VALUES('" + name + "', 'PK', '" + phone + "', '"
            + address + "', '" + cnic + "', 'Male', '" + username + "')";
           
            c.s.executeUpdate(passengerQuery);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created Successfully!");
            MainApp.showLoginScreen();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showLoginScreen();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
