package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddCustomerController {

    @FXML
    private TextField nameField, nationalityField, cnicField, addressField, phoneField;
    @FXML
    private RadioButton rbMale, rbFemale;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        try {
            // Load image if consistent with original path
            Image img = new Image(MainApp.class.getResourceAsStream("icons/emp.png"));
            imageView.setImage(img);
        } catch (Exception e) {
            System.out.println("Could not load image: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String cnic = cnicField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String gender = "";

        if (rbMale.isSelected())
            gender = "Male";
        else if (rbFemale.isSelected())
            gender = "Female";

        System.out.println("Saving Passenger: " + name + ", " + cnic); // Debug

        if (name.isEmpty() || nationality.isEmpty() || cnic.isEmpty() || address.isEmpty() || phone.isEmpty()
                || gender.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        // Relaxed validation: Allow 13 digits (standard) or 14 (if user formats
        // differently)
        if ((cnic.length() != 13) || !cnic.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "CNIC must be 13 digits (no dashes).");
            return;
        }

        if (phone.length() != 11 || !phone.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number must be 11 digits.");
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "INSERT INTO passenger VALUES('" + name + "', '" + nationality + "', '" + phone + "', '"
                    + address + "', '" + cnic + "', '" + gender + "')";
            conn.s.executeUpdate(query);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Details Added Successfully");
            clearFields();
            // Return to View Passengers list so user can see their addition
            MainApp.showViewPassengers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        // Return to View Passengers instead of Dashboard, as that's likely where they
        // came from
        MainApp.showViewPassengers();
    }

    private void clearFields() {
        nameField.clear();
        nationalityField.clear();
        cnicField.clear();
        addressField.clear();
        phoneField.clear();
        genderGroup.selectToggle(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
