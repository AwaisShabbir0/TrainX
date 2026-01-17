package airlinemanagementsystem.admin;

import airlinemanagementsystem.Conn;
import airlinemanagementsystem.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;

public class ViewPassengersController {

    @FXML
    private TableView<Passenger> passengersTable;
    @FXML
    private TableColumn<Passenger, String> colUsername;
    @FXML
    private TableColumn<Passenger, String> colName;
    @FXML
    private TableColumn<Passenger, String> colNationality;
    @FXML
    private TableColumn<Passenger, String> colPhone;
    @FXML
    private TableColumn<Passenger, String> colAddress;
    @FXML
    private TableColumn<Passenger, String> colCNIC;
    @FXML
    private TableColumn<Passenger, String> colGender;
    @FXML
    private TableColumn<Passenger, Void> colAction;

    private ObservableList<Passenger> passengers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colNationality.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNationality()));
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        colAddress.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        colCNIC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCnic()));
        colGender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));

        addDeleteButtonToTable();

        passengersTable.setItems(passengers);
        loadPassengers();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Passenger, Void>, TableCell<Passenger, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Passenger, Void> call(final TableColumn<Passenger, Void> param) {
                final TableCell<Passenger, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                        btn.setOnAction((event) -> {
                            Passenger data = getTableView().getItems().get(getIndex());
                            deletePassenger(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colAction.setCellFactory(cellFactory);
    }

    private void deletePassenger(Passenger passenger) {
        try {
            Conn c = new Conn();
            String query = "DELETE FROM passenger WHERE cnic = '" + passenger.getCnic() + "'";
            c.s.executeUpdate(query);

            // Also delete from users login table if username exists
            String username = passenger.getUsername();
            if (username != null && !username.isEmpty()) {
                String query2 = "DELETE FROM users WHERE username = '" + username + "'";
                c.s.executeUpdate(query2);
            }

            // Refresh list
            passengers.remove(passenger);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Passenger deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not delete passenger: " + e.getMessage());
        }
    }

    private void loadPassengers() {
        passengers.clear();
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM passenger";
            ResultSet rs = c.s.executeQuery(query);

            while (rs.next()) {
                String u = rs.getString("username");
                System.out.println("Loaded Passenger: " + rs.getString("name") + " | Username: " + u);
                passengers.add(new Passenger(
                        rs.getString("name"),
                        rs.getString("nationality"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("cnic"),
                        rs.getString("gender"),
                        u));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading passengers: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error Loading Data", "Could not load passenger list: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddPassenger() {
        MainApp.showAddCustomer();
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    // Inner Model Class
    public static class Passenger {
        private final String name;
        private final String nationality;
        private final String phone;
        private final String address;
        private final String cnic;
        private final String gender;
        private final String username;

        public Passenger(String name, String nationality, String phone, String address, String cnic, String gender, String username) {
            this.name = name;
            this.nationality = nationality;
            this.phone = phone;
            this.address = address;
            this.cnic = cnic;
            this.gender = gender;
            this.username = username;
        }
        
        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }

        public String getNationality() {
            return nationality;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getCnic() {
            return cnic;
        }

        public String getGender() {
            return gender;
        }
    }
}
