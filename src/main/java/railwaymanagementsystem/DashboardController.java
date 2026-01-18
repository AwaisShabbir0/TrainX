package railwaymanagementsystem;

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
            String displayName = user;
            try {
                Conn c = Conn.getInstance();
                java.sql.ResultSet rs = c.s.executeQuery("select full_name from users where username = '" + user + "'");
                if (rs.next()) {
                    displayName = rs.getString("full_name");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            welcomeLabel.setText("Welcome, " + displayName + " (" + role + ")");
        }

        updateDashboard(role);
    }

    private void updateDashboard(String role) {
        // Boolean flag to toggle visibility logic
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        // Admin Features
        setButtonVisible(btnAddTrain, isAdmin);
        setButtonVisible(btnViewTrains, isAdmin);
        setButtonVisible(btnPassengerInfo, isAdmin);
        setButtonVisible(btnKitchenOrders, isAdmin);
        setButtonVisible(btnAnalytics, isAdmin);

        // Customer Features (Inverse of Admin)
        setButtonVisible(btnBookTicket, !isAdmin);
        setButtonVisible(btnTrainSchedule, !isAdmin);
        setButtonVisible(btnJourneyHistory, !isAdmin);
        setButtonVisible(btnCancelTicket, !isAdmin);
        setButtonVisible(btnDownloadTicket, !isAdmin);
        setButtonVisible(btnProfile, !isAdmin);
        setButtonVisible(btnMeal, !isAdmin);
        setButtonVisible(btnWallet, !isAdmin);
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




