package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
//create a file on the filesystem and serve as the output destination for writing data, specifically a PDF file.

public class BoardingPass extends JFrame implements ActionListener {
    JTextField tfpnr;
    JLabel tfname, tfnationality, lblsrc, lbldest, labelfname, labelfcode, labeldate;
    JButton fetchButton, generatePDF;

    public BoardingPass() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Use java.awt.Font for GUI components
        java.awt.Font commonFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 16);

        JLabel heading = new JLabel("SkyLink Airways");
        heading.setBounds(380, 10, 450, 35);
        heading.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 32));
        add(heading);

        JLabel subheading = new JLabel("Boarding Pass");
        subheading.setBounds(360, 50, 300, 30);
        subheading.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 24));
        subheading.setForeground(Color.BLACK);
        add(subheading);

        JLabel lbCNIC = new JLabel("PNR DETAILS");
        lbCNIC.setBounds(60, 100, 150, 25);
        lbCNIC.setFont(commonFont);
        add(lbCNIC);

        tfpnr = new JTextField();
        tfpnr.setBounds(220, 100, 150, 25);
        add(tfpnr);

        fetchButton = new JButton("Enter");
        fetchButton.setBackground(Color.BLACK);
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setBounds(380, 100, 120, 25);
        fetchButton.addActionListener(this);
        add(fetchButton);

        JLabel lblname = new JLabel("NAME");
        lblname.setBounds(60, 140, 150, 25);
        lblname.setFont(commonFont);
        add(lblname);

        tfname = new JLabel();
        tfname.setBounds(220, 140, 150, 25);
        add(tfname);

        JLabel lblnationality = new JLabel("NATIONALITY");
        lblnationality.setBounds(60, 180, 150, 25);
        lblnationality.setFont(commonFont);
        add(lblnationality);

        tfnationality = new JLabel();
        tfnationality.setBounds(220, 180, 150, 25);
        add(tfnationality);

        JLabel lblsrcLabel = new JLabel("SRC");
        lblsrcLabel.setBounds(60, 220, 150, 25);
        lblsrcLabel.setFont(commonFont);
        add(lblsrcLabel);

        lblsrc = new JLabel();
        lblsrc.setBounds(220, 220, 150, 25);
        add(lblsrc);

        JLabel lbldestLabel = new JLabel("DEST");
        lbldestLabel.setBounds(380, 220, 150, 25);
        lbldestLabel.setFont(commonFont);
        add(lbldestLabel);

        lbldest = new JLabel();
        lbldest.setBounds(540, 220, 150, 25);
        add(lbldest);

        JLabel lblfname = new JLabel("Flight Name");
        lblfname.setBounds(60, 260, 150, 25);
        lblfname.setFont(commonFont);
        add(lblfname);

        labelfname = new JLabel();
        labelfname.setBounds(220, 260, 150, 25);
        add(labelfname);

        JLabel lblfcode = new JLabel("Flight Code");
        lblfcode.setBounds(380, 260, 150, 25);
        lblfcode.setFont(commonFont);
        add(lblfcode);

        labelfcode = new JLabel();
        labelfcode.setBounds(540, 260, 150, 25);
        add(labelfcode);

        JLabel lbldate = new JLabel("Date");
        lbldate.setBounds(60, 300, 150, 25);
        lbldate.setFont(commonFont);
        add(lbldate);

        labeldate = new JLabel();
        labeldate.setBounds(220, 300, 150, 25);
        add(labeldate);

        generatePDF = new JButton("Generate PDF");
        generatePDF.setBackground(Color.BLACK);
        generatePDF.setForeground(Color.WHITE);
        generatePDF.setBounds(380, 300, 150, 25);
        generatePDF.setEnabled(false);
        generatePDF.addActionListener(this);
        add(generatePDF);

        setSize(1000, 450);
        setLocation(300, 150);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchButton) {
            String pnr = tfpnr.getText().trim();
            if (pnr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a PNR!");
                return;
            }
            new Thread(() -> {
                try {
                    Conn conn = new Conn();
                    String query = "SELECT * FROM reservation WHERE PNR = ?";
                    PreparedStatement ps = conn.c.prepareStatement(query);
                    ps.setString(1, pnr);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        tfname.setText(rs.getString("name"));
                        tfnationality.setText(rs.getString("nationality"));
                        lblsrc.setText(rs.getString("src"));
                        lbldest.setText(rs.getString("des"));
                        labelfname.setText(rs.getString("flightname"));
                        labelfcode.setText(rs.getString("flightcode"));
                        labeldate.setText(rs.getString("ddate"));
                        generatePDF.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "No details found for the provided PNR!");
                        generatePDF.setEnabled(false);
                    }
                    rs.close();
                    ps.close();
                    conn.c.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error fetching data from the database!");
                }
            }).start();
        } else if (ae.getSource() == generatePDF) {
            String pnr = tfpnr.getText();
            generateBoardingPassPDF(pnr);
        }
    }
    public void generateBoardingPassPDF(String pnr) {
        try { 
            // Use com.itextpdf.text.Font for PDF generation
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 32, com.itextpdf.text.Font.BOLD);
            Document document = new Document(PageSize.A4);
            String fileName = "BP_" + pnr + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            // Create header
            Paragraph header = new Paragraph("SkyLink Airways - Boarding Pass", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            document.add(new Paragraph("\nPNR: " + pnr));
            document.add(new Paragraph("Passenger Name: " + tfname.getText()));
            document.add(new Paragraph("From: " + lblsrc.getText()));
            document.add(new Paragraph("To: " + lbldest.getText()));
            document.add(new Paragraph("Flight Name: " + labelfname.getText()));
            document.add(new Paragraph("Flight Code: " + labelfcode.getText()));
            document.add(new Paragraph("Date: " + labeldate.getText()));

            document.close();
            JOptionPane.showMessageDialog(null, "PDF Generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new BoardingPass();
    }
}