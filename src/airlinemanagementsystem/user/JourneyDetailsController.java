package airlinemanagementsystem.user;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class JourneyDetailsController {

    @FXML
    private TextField pnrField;
    @FXML
    private TableView<ObservableList<String>> detailsTable;

    @FXML
    private void handleShowDetails() {
        String input = pnrField.getText();
        if (input == null || input.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter PNR or CNIC.");
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "select * from reservation where PNR = '" + input + "' OR cnic = '" + input + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (!rs.isBeforeFirst()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No Information Found.");
                detailsTable.getItems().clear();
                return;
            }

            // Dynamic Table Column Creation
            detailsTable.getColumns().clear();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final int j = i - 1;
                String colName = metaData.getColumnName(i);
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                detailsTable.getColumns().add(col);
            }

            // Data Population
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    row.add(value != null ? value : "");
                }
                data.add(row);
            }
            detailsTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
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
}
