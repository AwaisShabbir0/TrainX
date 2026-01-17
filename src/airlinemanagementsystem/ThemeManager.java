package airlinemanagementsystem;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ThemeManager {
    // Colors
    public static final Color PRIMARY_COLOR = Color.web("#006400"); // Dark Green
    public static final Color SECONDARY_COLOR = Color.web("#F0FFF0"); // HoneyDew
    public static final Color TEXT_COLOR = Color.web("#323232"); // Dark Grey
    public static final Color ACCENT_COLOR = Color.web("#FF4500"); // Red-Orange
    public static final Color WHITE = Color.WHITE;

    // Fonts
    public static final Font HEADER_FONT = Font.font("Tahoma", FontWeight.BOLD, 36);
    public static final Font SUB_HEADER_FONT = Font.font("Tahoma", FontWeight.BOLD, 24);
    public static final Font LABEL_FONT = Font.font("Tahoma", FontWeight.NORMAL, 18);
    public static final Font BUTTON_FONT = Font.font("Tahoma", FontWeight.BOLD, 14);

    // Padding/Dimensions
    public static final double BUTTON_HEIGHT = 40.0;
    public static final double BUTTON_WIDTH = 150.0;
}
