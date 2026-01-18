package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import railwaymanagementsystem.ThemeManager;
import railwaymanagementsystem.utils.FareCalculator;
import railwaymanagementsystem.utils.ValidationUtils;
// MainApp import already there

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookTicketController {

    @FXML
    private TextField tfCNIC;
    @FXML
    private Label lblName, lblNationality, lblAddress, lblGender, lblTrainName, lblTrainCode, lblSelectedSeats;
    @FXML
    private ComboBox<String> cbSource, cbDestination, cbClass;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ImageView trainImage;

    private String matchedUsername = "";
    private List<String> selectedSeats = new ArrayList<>();

    @FXML
    public void initialize() {
        // Load Image
        // Load Image
        try {
            // Use absolute path to find the image in the artifacts/icons folder
            trainImage.setImage(new Image(
                    getClass().getResource("/railwaymanagementsystem/icons/train_icon.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Image load error in BookTicketController: " + e.getMessage());
        }

        // Initialize Class Choice
        cbClass.setItems(FXCollections.observableArrayList("Economy", "AC Standard", "AC Business"));
        cbClass.getSelectionModel().selectFirst();

        loadStations();
        autoFillCNIC();
    }

    private void autoFillCNIC() {
        try {
            Conn c = Conn.getInstance();
            String query = "select cnic from users where username = '" + MainApp.getCurrentUser() + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                tfCNIC.setText(rs.getString("cnic"));
                // Optional: handleFetchUser(); // Auto-fetch details too if desired
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStations() {
        try {
            Conn c = Conn.getInstance();
            String query = "select * from train";
            ResultSet rs = c.s.executeQuery(query);

            List<String> sources = new ArrayList<>();
            List<String> destinations = new ArrayList<>();

            while (rs.next()) {
                String s = rs.getString("source");
                String d = rs.getString("destination");
                if (!sources.contains(s))
                    sources.add(s);
                if (!destinations.contains(d))
                    destinations.add(d);
            }
            cbSource.setItems(FXCollections.observableArrayList(sources));
            cbDestination.setItems(FXCollections.observableArrayList(destinations));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFetchUser() {
        String cnic = tfCNIC.getText();
        if (cnic == null || cnic.length() != 13 || !cnic.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Invalid CNIC", "CNIC must be 13 digits.");
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String currentUser = MainApp.getCurrentUser();
            String query = "select * from users where cnic = '" + cnic + "' and username = '" + currentUser + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                matchedUsername = rs.getString("username");
                lblName.setText(rs.getString("full_name"));
                lblNationality.setText("Pakistani");
                lblAddress.setText(rs.getString("address"));
                lblGender.setText("N/A"); // Access via gender column if available
            } else {
                showAlert(Alert.AlertType.ERROR, "Access Denied", "You can only use your own CNIC (" + currentUser + ").");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFindTrains() {
        String src = cbSource.getValue();
        String dest = cbDestination.getValue();

        if (src == null || dest == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select source and destination.");
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "select * from train where source = '" + src + "' and destination = '" + dest + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                lblTrainName.setText(rs.getString("train_name"));
                lblTrainCode.setText(rs.getString("train_code"));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Trains", "No trains found for this route.");
                lblTrainName.setText("---");
                lblTrainCode.setText("---");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectSeats() {
        String tCode = lblTrainCode.getText();
        LocalDate date = dpDate.getValue();

        if (tCode.equals("---") || date == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "Please Select Train and Date first.");
            return;
        }

        if (!ValidationUtils.isValidFutureDate(date)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date", "You cannot book for a past date.");
            return;
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    MainApp.class.getResource("user/SeatSelection.fxml"));
            javafx.scene.Parent root = loader.load();

            SeatSelectionController controller = loader.getController();
            controller.setParentController(this);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Select Seats");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            controller.setDialogStage(stage);
            controller.setTrainDetails(tCode, date.toString());

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open seat selection: " + e.getMessage());
        }
    }

    public void setSeats(List<String> seats) {
        this.selectedSeats = new ArrayList<>(seats);
        lblSelectedSeats.setText(String.join(", ", selectedSeats));
    }

    @FXML
    private void handleBookTicket() {
        // Validate
        if (selectedSeats.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Seats", "Please select at least one seat.");
            return;
        }

        // inline refactoring

        if (dpDate.getValue() == null || !ValidationUtils.isValidFutureDate(dpDate.getValue())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date", "Please select a valid future date.");
            return;
        }

        String pnr = "PNR-" + new Random().nextInt(1000000);
        String name = lblName.getText();
        String nationality = lblNationality.getText();
        String trainname = lblTrainName.getText();
        String traincode = lblTrainCode.getText();
        String src = cbSource.getValue();
        String des = cbDestination.getValue();
        String sClass = cbClass.getValue();
        String ddate = dpDate.getValue().toString();

        try {
            Conn conn = Conn.getInstance();
            double basePrice = 0.0;
            String priceQuery = "SELECT price FROM train WHERE train_name = '" + trainname + "' AND train_code = '"
                    + traincode + "'";
            ResultSet priceResult = conn.s.executeQuery(priceQuery);
            if (priceResult.next()) {
                basePrice = priceResult.getDouble("price");
            }

            double totalAmount = FareCalculator.calculateTotalFare(basePrice, selectedSeats.size(), sClass);

            // Navigate to Payment
            MainApp.showPaymentScreen(pnr, totalAmount, selectedSeats, MainApp.getCurrentUser(),
                    tfCNIC.getText(), name, nationality, trainname, traincode,
                    src, des, ddate, sClass);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
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























