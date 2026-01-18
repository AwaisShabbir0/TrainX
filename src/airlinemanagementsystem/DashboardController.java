package airlinemanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnBookTicket;
    @FXML
    private Button btnTrainSchedule;
    @FXML
    private Button btnJourneyHistory;
    @FXML
    private Button btnCancelTicket;
    @FXML
    private Button btnDownloadTicket;
    @FXML
    private Button btnAddTrain;
    @FXML
    private Button btnViewTrains;
    @FXML
    private Button btnPassengerInfo;
    @FXML
    private Button btnKitchenOrders;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnMeal;
    @FXML
    private Button btnWallet;
    @FXML
    private Button btnAnalytics;

    @FXML
    public void initialize() {
        // Get user info from MainApp session
        String user = MainApp.getCurrentUser();
        String role = MainApp.getUserRole();

        if (user != null) {
            welcomeLabel.setText("Welcome, " + user + " (" + role + ")");
        }

        updateDashboard(role);
    }

    private void updateDashboard(String role) {
        if ("Admin".equalsIgnoreCase(role)) {
            setButtonVisible(btnAddTrain, true);
            setButtonVisible(btnViewTrains, true);
            setButtonVisible(btnPassengerInfo, true);
            setButtonVisible(btnKitchenOrders, true);
            setButtonVisible(btnAnalytics, true);

            setButtonVisible(btnBookTicket, false);
            setButtonVisible(btnTrainSchedule, false);
            setButtonVisible(btnJourneyHistory, false);
            setButtonVisible(btnCancelTicket, false);
            setButtonVisible(btnDownloadTicket, false);
            setButtonVisible(btnProfile, false);
            setButtonVisible(btnMeal, false);
            setButtonVisible(btnWallet, false);
        } else {
            // Customer
            setButtonVisible(btnAddTrain, false);
            setButtonVisible(btnViewTrains, false);
            setButtonVisible(btnPassengerInfo, false);
            setButtonVisible(btnKitchenOrders, false);
            setButtonVisible(btnAnalytics, false);

            setButtonVisible(btnBookTicket, true);
            setButtonVisible(btnTrainSchedule, true);
            setButtonVisible(btnJourneyHistory, true);
            setButtonVisible(btnCancelTicket, true);
            setButtonVisible(btnDownloadTicket, true);
            setButtonVisible(btnProfile, true);
            setButtonVisible(btnMeal, true);
            setButtonVisible(btnWallet, true);
        }
    }

    private void setButtonVisible(Button btn, boolean visible) {
        if (btn != null) {
            btn.setVisible(visible);
            btn.setManaged(visible);
        }
    }

    @FXML
    private void handleBookTicket() {
        MainApp.showBookTicket();
    }
    
    @FXML
    private void handleAnalytics() {
        MainApp.showAnalytics();
    }

    @FXML
    private void handleTrainSchedule() {
        MainApp.showTrainSchedule();
    }

    @FXML
    private void handleJourneyHistory() {
        MainApp.showJourneyDetails();
    }

    @FXML
    private void handleCancelTicket() {
        MainApp.showCancelTicket();
    }

    @FXML
    private void handleDownloadTicket() {
        MainApp.showTicketScreen();
    }

    @FXML
    private void handleAddTrain() {
        MainApp.showAddTrain();
    }

    @FXML
    private void handleViewTrains() {
        MainApp.showTrainDetails();
    }

    @FXML
    private void handlePassengerInfo() {
        MainApp.showViewPassengers();
    }

    @FXML
    private void handleKitchenOrders() {
        MainApp.showKitchenOrders();
    }

    @FXML
    private void handleProfile() {
        MainApp.showUserProfile();
    }

    @FXML
    private void handleBookMeal() {
        MainApp.showBookMeal();
    }

    @FXML
    private void handleWallet() {
        MainApp.showWallet();
    }

    @FXML
    private void handleLogout() {
        MainApp.showLoginScreen();
    }
}
