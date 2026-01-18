package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPassengerController {

    @FXML
    private TextField cnicField, nameField, nationalityField, phoneField, addressField, genderField;

    private Stage dialogStage;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setPassengerData(ViewPassengersController.Passenger passenger) {
        cnicField.setText(passenger.getCnic());
        nameField.setText(passenger.getName());
        nationalityField.setText(passenger.getNationality());
        phoneField.setText(passenger.getPhone());
        addressField.setText(passenger.getAddress());
        genderField.setText(passenger.getGender());
    }

    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            try {
                Conn c = new Conn();
                String query = "UPDATE passenger SET name = '" + nameField.getText() + "', nationality = '"
                        + nationalityField.getText() + "', phone = '" + phoneField.getText() + "', address = '"
                        + addressField.getText() + "', gender = '" + genderField.getText() 
                        + "' WHERE cnic = '" + cnicField.getText() + "'";
                
                c.s.executeUpdate(query);
                
                okClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not update passenger: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().length() == 0) {
            errorMessage += "No valid phone!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
