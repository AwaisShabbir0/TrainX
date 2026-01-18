package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTrainController {

    @FXML
    private TextField trainCodeField, trainNameField, sourceField, destField, fairField, depTimeField, arrTimeField;

    private Stage dialogStage;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setTrainData(ObservableList<String> trainData) {
        trainCodeField.setText(trainData.get(0));
        trainNameField.setText(trainData.get(1));
        sourceField.setText(trainData.get(2));
        destField.setText(trainData.get(3));
        fairField.setText(trainData.get(4));
        depTimeField.setText(trainData.get(5));
        arrTimeField.setText(trainData.get(6));
    }

    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            try {
                Conn c = new Conn();
                String query = "UPDATE train SET train_name = '" + trainNameField.getText() + "', source = '"
                        + sourceField.getText() + "', destination = '" + destField.getText() + "', price = '"
                        + fairField.getText() + "', departure_time = '" + depTimeField.getText() 
                        + "', arrival_time = '" + arrTimeField.getText() + "' WHERE train_code = '" + trainCodeField.getText() + "'";
                
                c.s.executeUpdate(query);
                
                okClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not update train: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (trainNameField.getText() == null || trainNameField.getText().length() == 0) {
            errorMessage += "No valid train name!\n";
        }
        if (sourceField.getText() == null || sourceField.getText().length() == 0) {
            errorMessage += "No valid source!\n";
        }
        if (destField.getText() == null || destField.getText().length() == 0) {
            errorMessage += "No valid destination!\n";
        }
        if (fairField.getText() == null || fairField.getText().length() == 0) {
            errorMessage += "No valid price!\n";
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
