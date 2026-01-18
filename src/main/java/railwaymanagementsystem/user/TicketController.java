package railwaymanagementsystem.user;

import railwaymanagementsystem.Conn;
import railwaymanagementsystem.MainApp;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketController {

    @FXML
    private TextField pnrField;
    @FXML
    private Label nameLabel, nationalityLabel, srcLabel, destLabel, trainNameLabel, trainCodeLabel, dateLabel;
    @FXML
    private Button btnGeneratePDF;

    @FXML
    private void handleFetch() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a PNR!");
            return;
        }

        try {
            Conn conn = new Conn();
            String currentUser = MainApp.getCurrentUser();
            String query = "SELECT * FROM reservation WHERE PNR = ? AND account_username = ?";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, pnr);
            ps.setString(2, currentUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                nationalityLabel.setText(rs.getString("nationality"));
                srcLabel.setText(rs.getString("src"));
                destLabel.setText(rs.getString("des"));
                trainNameLabel.setText(rs.getString("train_name"));
                trainCodeLabel.setText(rs.getString("train_code"));
                dateLabel.setText(rs.getString("ddate"));
                btnGeneratePDF.setDisable(false);
            } else {
                 if (pnr.length() == 13 && pnr.matches("\\d+")) {
                     showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter the PNR (e.g. PNR-XXXX), not your CNIC.\nYou can find your PNR in the Journey History.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Ticket with this PNR not found for your account.");
                }
                clearLabels();
                btnGeneratePDF.setDisable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleGeneratePDF() {
        String pnr = pnrField.getText();
        try {
            String fileName = "Ticket_" + pnr + ".pdf";
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA,
                    24, com.itextpdf.text.Font.BOLD);
            Paragraph header = new Paragraph("TrainX - Ticket", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("PNR: " + pnr));
            document.add(new Paragraph("Passenger Name: " + nameLabel.getText()));
            document.add(new Paragraph("From: " + srcLabel.getText()));
            document.add(new Paragraph("To: " + destLabel.getText()));
            document.add(new Paragraph("Train Name: " + trainNameLabel.getText()));
            document.add(new Paragraph("Train Code: " + trainCodeLabel.getText()));
            document.add(new Paragraph("Travel Date: " + dateLabel.getText()));

            document.add(new Paragraph("\nThank you for choosing TrainX!"));

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket Generated: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "PDF Error", "Could not generate PDF: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        MainApp.showDashboard(MainApp.getCurrentUser(), MainApp.getUserRole());
    }

    private void clearLabels() {
        nameLabel.setText("---");
        nationalityLabel.setText("---");
        srcLabel.setText("---");
        destLabel.setText("---");
        trainNameLabel.setText("---");
        trainCodeLabel.setText("---");
        dateLabel.setText("---");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
