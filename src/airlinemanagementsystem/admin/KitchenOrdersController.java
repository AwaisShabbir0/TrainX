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

public class KitchenOrdersController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, String> colPNR, colItems, colAmount;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colPNR.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPnr()));
        colItems.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItems()));
        colAmount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmount()));

        ordersTable.setItems(orders);
        loadOrders();
    }

    private void loadOrders() {
        orders.clear();
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM catering";
            ResultSet rs = c.s.executeQuery(query);

            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("pnr"),
                        rs.getString("items"),
                        rs.getString("amount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    // Inner Data Class
    public static class Order {
        private final String pnr;
        private final String items;
        private final String amount;

        public Order(String pnr, String items, String amount) {
            this.pnr = pnr;
            this.items = items;
            this.amount = amount;
        }

        public String getPnr() {
            return pnr;
        }

        public String getItems() {
            return items;
        }

        public String getAmount() {
            return amount;
        }
    }
}
