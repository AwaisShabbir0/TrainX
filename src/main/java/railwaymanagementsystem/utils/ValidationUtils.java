package railwaymanagementsystem.utils;

import java.util.regex.Pattern;
import java.time.LocalDate;

public class ValidationUtils {

    // Regex Patterns
    private static final Pattern CARD_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^\\d{3}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern CNIC_PATTERN = Pattern.compile("^\\d{13}$");
    // Time format HH:mm (24-hour)
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

    /**
     * Checks if the card number is exactly 16 digits.
     */
    public static boolean isValidCard(String card) {
        if (card == null) return false;
        return CARD_PATTERN.matcher(card).matches();
    }

    /**
     * Checks if the CVV is exactly 3 digits.
     */
    public static boolean isValidCVV(String cvv) {
        if (cvv == null) return false;
        return CVV_PATTERN.matcher(cvv).matches();
    }

    /**
     * Checks if the phone number is exactly 11 digits.
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Checks if the CNIC is exactly 13 digits.
     */
    public static boolean isValidCNIC(String cnic) {
        if (cnic == null) return false;
        return CNIC_PATTERN.matcher(cnic).matches();
    }

    /**
     * Checks if the string is a valid positive number (integer or decimal).
     */
    public static boolean isPositiveNumber(String str) {
        if (str == null) return false;
        try {
            double val = Double.parseDouble(str);
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the time string is in HH:mm format.
     */
    public static boolean isValidTime(String time) {
        if (time == null) return false;
        return TIME_PATTERN.matcher(time).matches();
    }

    /**
     * Checks if the date is in the future (or today).
     */
    public static boolean isValidFutureDate(LocalDate date) {
        if (date == null) return false;
        return !date.isBefore(LocalDate.now());
    }
}
