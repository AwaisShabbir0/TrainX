package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.ResultSet;

public class TrainDetailsController {

    @FXML
    private TableView<ObservableList<String>> trainTable;
    @FXML
    private TableColumn<ObservableList<String>, String> colTrainCode;
    @FXML
    private TableColumn<ObservableList<String>, String> colTrainName;
    @FXML
    private TableColumn<ObservableList<String>, String> colSource;
    @FXML
    private TableColumn<ObservableList<String>, String> colDest;
    @FXML
    private TableColumn<ObservableList<String>, String> colFair;

    @FXML
    public void initialize() {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colTrainCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colTrainName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colSource.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colDest.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colFair.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
    }

    private void loadData() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try {
            Conn conn = new Conn();
            // Assuming table 'train' has columns: train_code, train_name, source,
            // destination, fair
            // Adjust query if columns are different
            String query = "select * from train";
            ResultSet rs = conn.s.executeQuery(query);

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("train_code"));
                row.add(rs.getString("train_name"));
                row.add(rs.getString("source"));
                row.add(rs.getString("destination"));
                row.add(rs.getString("fair"));
                data.add(row);
            }

            trainTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }
}
