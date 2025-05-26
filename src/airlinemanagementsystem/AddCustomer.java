package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; // handle all performed actions
import airlinemanagementsystem.Conn;
public class AddCustomer extends JFrame implements ActionListener {
    JTextField tfname, tfphone, tfCNIC, tfnationality, tfaddress;
    JRadioButton rbmale, rbfemale;
    JButton save;
    public AddCustomer() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("ADD CUSTOMER DETAILS");
        heading.setBounds(220, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        heading.setForeground(Color.BLACK);
        add(heading);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(60, 80, 150, 25);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblname);

        tfname = new JTextField();
        tfname.setBounds(220, 80, 150, 25);
        add(tfname);

        JLabel lblnationality = new JLabel("Nationality");
        lblnationality.setBounds(60, 130, 150, 25);
        lblnationality.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblnationality);

        tfnationality = new JTextField();
        tfnationality.setBounds(220, 130, 150, 25);
        add(tfnationality);

        JLabel lbCNIC = new JLabel("CNIC Number");
        lbCNIC.setBounds(60, 180, 150, 25);
        lbCNIC.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbCNIC);

        tfCNIC = new JTextField();
        tfCNIC.setBounds(220, 180, 150, 25);
        add(tfCNIC);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(60, 230, 150, 25);
        lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(220, 230, 150, 25);
        add(tfaddress);

        JLabel lblgender = new JLabel("Gender");
        lblgender.setBounds(60, 280, 150, 25);
        lblgender.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblgender);

        ButtonGroup gendergroup = new ButtonGroup();

        rbmale = new JRadioButton("Male");
        rbmale.setBounds(220, 280, 70, 25);
        rbmale.setBackground(Color.WHITE);
        add(rbmale);

        rbfemale = new JRadioButton("Female");
        rbfemale.setBounds(300, 280, 70, 25);
        rbfemale.setBackground(Color.WHITE);
        add(rbfemale);

        gendergroup.add(rbmale);
        gendergroup.add(rbfemale);

        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(60, 330, 150, 25);
        lblphone.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(220, 330, 150, 25);
        add(tfphone);

        save = new JButton("SAVE");
        save.setBackground(Color.BLACK);
        save.setForeground(Color.WHITE);
        save.setBounds(220, 380, 150, 30);
        save.addActionListener(this);
        add(save);

        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/emp.png"));
        JLabel lblimage = new JLabel(image);
        lblimage.setBounds(450, 80, 280, 400);
        add(lblimage);

        setSize(900, 600);
        setLocation(300, 150);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        final String name = tfname.getText();
        final String nationality = tfnationality.getText();
        final String phone = tfphone.getText();
        final String address = tfaddress.getText();
        final String CNIC = tfCNIC.getText();
        final String gender = rbmale.isSelected() ? "Male" : "Female";
        if (CNIC.length() != 14 || !CNIC.matches("\\d+")) { //Ensuring the CNIC consists only of digits 
            JOptionPane.showMessageDialog(null, "CNIC must be 14 digits.");
            return;
        }
        if (phone.length() != 11 || !phone.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Phone number must be 11 digits.");
            return;
        }
        save.setEnabled(false); // Disable the button to prevent multiple clicks
        new Thread(() -> {
            try {
                Conn conn = new Conn();
                
                String query = "INSERT INTO passenger VALUES('" + name + "', '" + 
                              nationality + "', '" + phone + "', '" + 
                              address + "', '" + CNIC + "', '" + gender + "')";
                
                conn.s.executeUpdate(query);
                // Graphical user interface (GUI) are executed on the Event Dispatch Thread (EDT).
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Customer Details Added Successfully");
                    save.setEnabled(true);
                    setVisible(false);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    save.setEnabled(true);
                });
                e.printStackTrace();
            }
        }).start();
    }
    public static void main(String[] args) {
        new AddCustomer();
    }
}