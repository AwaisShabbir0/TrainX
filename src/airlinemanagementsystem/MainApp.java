package airlinemanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static String currentUser;
    private static String userRole;

    @Override
    public void start(Stage primaryStage) {
        // Run DB Update/Check on startup to fix schema issues
        new airlinemanagementsystem.admin.UpdateDB().main(null);
        
        MainApp.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icons/trainx_logo.png")));
        showLoginScreen();
    }

    public static void showLoginScreen() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("Login.fxml"));
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDashboard(String username, String role) {
        currentUser = username;
        userRole = role;
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("Dashboard.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Dashboard.fxml: " + e.getMessage());
        }
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void showSignupScreen() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("user/Signup.fxml"));
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Signup");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBookTicket() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("user/BookTicket.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Book Ticket");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showJourneyDetails() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("user/JourneyDetails.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Journey Details");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTrainSchedule() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("user/TrainSchedule.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Train Schedule");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showPaymentScreen(String pnr, double amount, java.util.List<String> seats,
            String username, String cnic, String name, String nationality,
            String trainName, String trainCode, String src, String des,
            String date, String sClass) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/Payment.fxml"));
            Parent root = loader.load();

            airlinemanagementsystem.user.PaymentController controller = loader.getController();
            controller.setBookingDetails(pnr, amount, seats, username, cnic, name, nationality,
                    trainName, trainCode, src, des, date, sClass);

            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Payment");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTicketScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/Ticket.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Download Ticket");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCancelTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/Cancel.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Cancel Ticket");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("admin/AddCustomer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Add Passenger");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showViewPassengers() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("admin/ViewPassengers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Passenger List");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTrainDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("admin/TrainDetails.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Train Details (Admin)");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUserProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/UserProfile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - My Profile");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBookMeal() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/BookMeal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Book Meal");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showWallet() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("user/Wallet.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - E-Wallet");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAddTrain() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("admin/AddTrain.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Add Train");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKitchenOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("admin/KitchenOrders.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());

            primaryStage.setTitle("TrainX Management System - Kitchen Orders");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
