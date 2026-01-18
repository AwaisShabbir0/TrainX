package railwaymanagementsystem;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private ComboBox<String> roleChoice;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupButton;

    @FXML
    public void initialize() {
        // Initialize Role Choice
        roleChoice.setItems(FXCollections.observableArrayList("Customer", "Admin"));
        roleChoice.getSelectionModel().selectFirst();

        // Add listener to hide/show signup based on role (mirrors Swing logic)
        roleChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            boolean isCustomer = "Customer".equals(newValue);
            signupButton.setVisible(isCustomer);
            signupButton.setManaged(isCustomer);
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleChoice.getValue();

        try {
            Conn c = Conn.getInstance();
            String query;
            if (role.equals("Admin")) {
                query = "select * from login where username = '" + username + "' and password = '" + password + "'";
            } else {
                query = "select * from users where username = '" + username + "' and password = '" + password + "'";
            }

            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                // Successful Login
                MainApp.showDashboard(username, role);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Incorrect username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to database.");
        }
    }

    @FXML
    private void handleSignup() {
        MainApp.showSignupScreen();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
