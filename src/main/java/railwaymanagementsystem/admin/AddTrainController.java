package railwaymanagementsystem.admin;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import railwaymanagementsystem.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.ResultSet;

public class AddTrainController {

    @FXML
    private TextField trainCodeField, trainNameField, sourceField, destField, fairField, depTimeField, arrTimeField;

    @FXML
    public void initialize() {
        createColumnsIfNotExists();
    }

    private void createColumnsIfNotExists() {
        try {
            Conn c = new Conn();
            // Check if column exists, if not add it
            // This is a bit tricky in pure SQL without metadata inspection logic
            // complexity,
            // so we'll try a safe ADD COLUMN command that ignores if exists or check meta

            // Simpler approach: Just try to alter table, catch exception if it fails (not
            // ideal but works for quick patches)
            // Or better: Use the DBInspector logic.
            // Let's just run alter table. MySQL supports "ADD COLUMN IF NOT EXISTS" only in
            // newer versions.
            // We will standard try-catch blocks for each column.

            try {
                c.s.executeUpdate("ALTER TABLE train ADD COLUMN departure_time VARCHAR(20)");
            } catch (Exception e) {
                /* Ignore if exists */ }

            try {
                c.s.executeUpdate("ALTER TABLE train ADD COLUMN arrival_time VARCHAR(20)");
            } catch (Exception e) {
                /* Ignore if exists */ }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        String code = trainCodeField.getText().trim();
        String name = trainNameField.getText();
        String src = sourceField.getText();
        String dest = destField.getText();
        String fair = fairField.getText();
        String dep = depTimeField.getText();
        String arr = arrTimeField.getText();

        if (code.isEmpty() || name.isEmpty() || src.isEmpty() || dest.isEmpty() || fair.isEmpty() || dep.isEmpty()
                || arr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        if (!ValidationUtils.isPositiveNumber(fair)) {
             showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a valid number.");
             return;
        }

        // Simple Time Validation HH:mm
        if (!ValidationUtils.isValidTime(dep) || !ValidationUtils.isValidTime(arr)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Time must be in HH:mm format (e.g., 14:30).");
            return;
        }

        try {
            Conn conn = new Conn();
            // Check if row already exists
            String check = "SELECT * FROM train WHERE train_code = '" + code + "'";
            if (conn.s.executeQuery(check).next()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Train Code already exists!");
                return;
            }

            // Using simple statement. Note: The train table structure must match!
            // If the table was just altered, the column order ends with dep, arr.
            // Original: code, name, source, dest, price
            // New: code, name, source, dest, price, dep, arr

            String query = "INSERT INTO train (train_code, train_name, source, destination, price, departure_time, arrival_time) VALUES('"
                    + code + "', '" + name + "', '" + src + "', '" + dest + "', '"
                    + fair + "', '" + dep + "', '" + arr + "')";
            conn.s.executeUpdate(query);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Train Added Successfully");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    private void clearFields() {
        trainCodeField.clear();
        trainNameField.clear();
        sourceField.clear();
        destField.clear();
        fairField.clear();
        depTimeField.clear();
        arrTimeField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
