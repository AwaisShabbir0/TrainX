package airlinemanagementsystem.user;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class TrainScheduleController {

    @FXML
    private TableView<ObservableList<String>> scheduleTable;

    @FXML
    public void initialize() {
        loadSchedule();
    }

    private void loadSchedule() {
        try {
            Conn conn = new Conn();
            String query = "select * from train";
            ResultSet rs = conn.s.executeQuery(query);

            // Dynamic columns
            scheduleTable.getColumns().clear();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final int j = i - 1;
                String colName = metaData.getColumnName(i);
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                scheduleTable.getColumns().add(col);
            }

            // Data
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    row.add(value != null ? value : "");
                }
                data.add(row);
            }
            scheduleTable.setItems(data);

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
