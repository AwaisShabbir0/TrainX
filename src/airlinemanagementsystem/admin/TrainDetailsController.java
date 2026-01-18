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
    private TableColumn<ObservableList<String>, String> colDepTime;
    @FXML
    private TableColumn<ObservableList<String>, String> colArrTime;
    @FXML
    private TableColumn<ObservableList<String>, Void> colAction;

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
        colDepTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        colArrTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));
        
        addDeleteButtonToTable();
    }

    private void addDeleteButtonToTable() {
        javafx.util.Callback<TableColumn<ObservableList<String>, Void>, javafx.scene.control.TableCell<ObservableList<String>, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public javafx.scene.control.TableCell<ObservableList<String>, Void> call(final TableColumn<ObservableList<String>, Void> param) {
                final javafx.scene.control.TableCell<ObservableList<String>, Void> cell = new javafx.scene.control.TableCell<>() {
                    private final javafx.scene.control.Button btnDelete = new javafx.scene.control.Button("Delete");
                    private final javafx.scene.control.Button btnEdit = new javafx.scene.control.Button("Edit");
                    private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(btnEdit, btnDelete);

                    {
                        pane.setSpacing(10);
                        pane.setAlignment(javafx.geometry.Pos.CENTER);
                        
                        btnDelete.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                        btnDelete.setOnAction((event) -> {
                            ObservableList<String> data = getTableView().getItems().get(getIndex());
                            String trainCode = data.get(0); 
                            deleteTrain(trainCode, data);
                        });

                        btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        btnEdit.setOnAction((event) -> {
                             ObservableList<String> data = getTableView().getItems().get(getIndex());
                             openEditDialog(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };

        colAction.setCellFactory(cellFactory);
    }
    
    private void openEditDialog(ObservableList<String> trainData) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(MainApp.class.getResource("admin/EditTrain.fxml"));
            javafx.scene.layout.BorderPane page = loader.load();

            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Edit Train");
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.primaryStage);
            javafx.scene.Scene scene = new javafx.scene.Scene(page);
            dialogStage.setScene(scene);

            EditTrainController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTrainData(trainData);

            dialogStage.showAndWait();
            
            if (controller.isOkClicked()) {
                loadData(); // Refresh table
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Could not open edit dialog: " + e.getMessage());
        }
    }

    private void deleteTrain(String trainCode, ObservableList<String> row) {
        try {
            Conn c = new Conn();
            // Assuming the column name in DB is 'train_code' based on loadData method
            String query = "DELETE FROM train WHERE train_code = '" + trainCode + "'";
            c.s.executeUpdate(query);

            // Refresh list
            trainTable.getItems().remove(row);
            showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Train deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Could not delete train: " + e.getMessage());
        }
    }

    private void loadData() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try {
            Conn conn = new Conn();
            String query = "select * from train";
            ResultSet rs = conn.s.executeQuery(query);

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                // Ensure these column names match your DB schema exactly.
                // If the user says they aren't showing, maybe the column names are different or table is empty?
                // I'll assume standard naming but keeping an eye out.
                row.add(rs.getString("train_code"));
                row.add(rs.getString("train_name"));
                row.add(rs.getString("source"));
                row.add(rs.getString("destination"));
                row.add(rs.getString("price"));
                row.add(rs.getString("departure_time"));
                row.add(rs.getString("arrival_time"));
                data.add(row);
            }

            trainTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error Loading Data", "Could not load train list: " + e.getMessage());
        }
    }
    
    private void showAlert(javafx.scene.control.Alert.AlertType type, String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }
}
