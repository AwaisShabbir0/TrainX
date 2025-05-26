package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Payment extends JFrame implements ActionListener {
    private JTextField tfCardNumber, tfCardHolderName, tfExpiryDate, tfCVV;
    private JButton btnPay;
    private String pnr; 
    private double amount; 

    public Payment(String pnr, double amount) {
        this.pnr = pnr; 
        this.amount = amount;
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblCardNumber = new JLabel("Card Number:");
        lblCardNumber.setBounds(50, 50, 150, 25);
        lblCardNumber.setFont(new Font("Tahoma", Font.PLAIN, 16)); 
        add(lblCardNumber);

        tfCardNumber = new JTextField();
        tfCardNumber.setBounds(200, 50, 200, 25);
        add(tfCardNumber);

        JLabel lblCardHolderName = new JLabel("Card Holder Name:");
        lblCardHolderName.setBounds(50, 100, 150, 25);
        lblCardHolderName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblCardHolderName);

        tfCardHolderName = new JTextField();
        tfCardHolderName.setBounds(200, 100, 200, 25);
        add(tfCardHolderName);

        JLabel lblExpiryDate = new JLabel("Expiry Date (MM/YY):");
        lblExpiryDate.setBounds(50, 150, 150, 25);
        lblExpiryDate.setFont(new Font("Tahoma", Font.PLAIN, 16)); 
        add(lblExpiryDate);

        tfExpiryDate = new JTextField();
        tfExpiryDate.setBounds(200, 150, 200, 25);
        add(tfExpiryDate);

        JLabel lblCVV = new JLabel("CVV:");
        lblCVV.setBounds(50, 200, 150, 25);
        lblCVV.setFont(new Font("Tahoma", Font.PLAIN, 16)); 
        add(lblCVV);

        tfCVV = new JTextField();
        tfCVV.setBounds(200, 200, 200, 25);
        add(tfCVV);

        btnPay = new JButton("Pay");
        btnPay.setBounds(200, 250, 100, 30);
        btnPay.addActionListener(this);
        add(btnPay);

        setSize(500, 350);
        setLocation(300, 200);
        setVisible(true);
    }

    public Payment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionPerformed(ActionEvent ae) {
        String cardNumber = tfCardNumber.getText();
        String cardHolderName = tfCardHolderName.getText();
        String expiryDate = tfExpiryDate.getText();
        String cvv = tfCVV.getText();

        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all details.");
            return;
        }

        if (!cardNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Card number must be 16 digits.");
            return;
        }

        if (!cvv.matches("\\d{3,4}")) {
            JOptionPane.showMessageDialog(this, "CVV must be 3 or 4 digits.");
            return;
        }

        // Save payment details to the database
        try {
            Conn conn = new Conn();
            String query = "INSERT INTO payments (pnr, card_number, card_holder_name, expiry_date, cvv, amount) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, pnr);
            ps.setString(2, cardNumber);
            ps.setString(3, cardHolderName);
            ps.setString(4, expiryDate);
            ps.setString(5, cvv);
            ps.setDouble(6, amount); 
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payment successful!");
            setVisible(false); 
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Payment failed!");
        }
    }

    public static void main(String[] args) {
        new Payment();
    }
}