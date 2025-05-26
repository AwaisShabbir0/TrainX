package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
public class Home extends JFrame implements ActionListener {
    private JButton flightDetails, customerDetails, bookFlight, journeyDetails, 
                    ticketCancellation, boardingPass;
    public Home() {
        setLayout(null);
        ImageIcon originalIcon = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/front.jpg"));
        JLabel image = new JLabel(originalIcon);
        image.setBounds(0, 0, getWidth(), getHeight());
        add(image);

        // Heading
        JLabel heading = new JLabel("SkyLink Airways");
        heading.setForeground(new Color(0, 51, 102));
        heading.setFont(new Font("Tahoma", Font.BOLD, 36));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBounds(100, 20, getWidth() - 200, 50);
        image.add(heading);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(new Color(255, 255, 255, 180)); 
        buttonPanel.setBounds((getWidth() - 300) / 2, 100, 300, 400);
        image.add(buttonPanel);
        buttonPanel.add(createStyledButton("Flight Details"));
        buttonPanel.add(createStyledButton("Add Customer Details"));
        buttonPanel.add(createStyledButton("Book Flight"));
        buttonPanel.add(createStyledButton("Journey Details"));
        buttonPanel.add(createStyledButton("Cancel Ticket"));
        buttonPanel.add(createStyledButton("Boarding Pass"));
        setSize(800, 600); 
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                image.setBounds(0, 0, getWidth(), getHeight());
                heading.setBounds(100, 20, getWidth() - 200, 50); 
                buttonPanel.setBounds((getWidth() - 300) / 2, 100, 250, 400); //resize according to window size
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.PLAIN, 16));
        button.setForeground(new Color(51, 51, 51));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false); // Disables the focus indicator.
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
                button.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(51, 51, 51));
            }
        });

        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        String text = ae.getActionCommand();

        switch (text) {
            case "Add Customer Details":
                new AddCustomer();
                break;
            case "Flight Details":
                new FlightInfo();
                break;
            case "Book Flight":
                new BookFlight();
                break;
            case "Journey Details":
                new JourneyDetails();
                break;
            case "Cancel Ticket":
                new Cancel();
                break;
            case "Boarding Pass":
                new BoardingPass();
                break;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Home());
    }
}
