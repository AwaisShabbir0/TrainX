package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionController {

    @FXML
    private Label headerLabel;
    @FXML
    private GridPane seatGrid;
    @FXML
    private Label statusLabel;

    private List<String> selectedSeats = new ArrayList<>();
    private List<String> bookedSeats = new ArrayList<>();
    private BookTicketController parentController;
    private Stage dialogStage;
    private String trainCode;
    private String date;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParentController(BookTicketController parentController) {
        this.parentController = parentController;
    }

    public void setTrainDetails(String trainCode, String date) {
        this.trainCode = trainCode;
        this.date = date;
        headerLabel.setText("Select Seats (" + trainCode + " - " + date + ")");
        loadBookedSeats();
        generateSeats();
    }

    private void loadBookedSeats() {
        bookedSeats.clear();
        try {
            Conn conn = Conn.getInstance();
            String query = "select seats from reservation where train_code = '" + trainCode + "' AND ddate = '" + date
                    + "'";
            ResultSet rs = conn.s.executeQuery(query);
            while (rs.next()) {
                String s = rs.getString("seats");
                if (s != null && !s.isEmpty()) {
                    String[] split = s.split(", ");
                    for (String p : split) {
                        bookedSeats.add(p.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateSeats() {
        seatGrid.getChildren().clear();
        int rows = 10;
        int cols = 4;

        for (int i = 1; i <= 40; i++) {
            String seatNo = "A-" + i;
            ToggleButton btn = new ToggleButton(seatNo);
            btn.setPrefSize(60, 40);

            if (bookedSeats.contains(seatNo)) {
                btn.setDisable(true);
                btn.setStyle("-fx-base: red;");
            } else {
                btn.setOnAction(e -> handleSeatToggle(btn, seatNo));
            }

            int row = (i - 1) / cols;
            int col = (i - 1) % cols;
            // Add a gap in the middle for aisle (after col 1)
            if (col >= 2)
                col++;

            seatGrid.add(btn, col, row);
        }
    }

    private void handleSeatToggle(ToggleButton btn, String seatNo) {
        if (btn.isSelected()) {
            if (selectedSeats.size() >= 5) {
                btn.setSelected(false);
                showAlert("Limit Reached", "You can only select up to 5 seats.");
                return;
            }
            selectedSeats.add(seatNo);
            btn.setStyle("-fx-base: green;");
        } else {
            selectedSeats.remove(seatNo);
            btn.setStyle("");
        }
        statusLabel.setText("Selected: " + selectedSeats.size());
    }

    @FXML
    private void handleConfirm() {
        if (selectedSeats.isEmpty()) {
            showAlert("No Selection", "Please select at least one seat.");
            return;
        }
        if (parentController != null) {
            parentController.setSeats(selectedSeats);
        }
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
