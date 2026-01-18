package railwaymanagementsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import railwaymanagementsystem.utils.ValidationUtils;

public class ValidationUtilsTest {

    @Test
    public void testIsValidCard() {
        assertTrue(ValidationUtils.isValidCard("1234567812345678"), "16 digits should be valid");
        assertFalse(ValidationUtils.isValidCard("1234"), "4 digits should be invalid");
        assertFalse(ValidationUtils.isValidCard("abcdefghijklmnop"), "Non-digits should be invalid");
        assertFalse(ValidationUtils.isValidCard(null), "Null should be invalid");
    }

    @Test
    public void testIsValidCVV() {
        assertTrue(ValidationUtils.isValidCVV("123"), "3 digits should be valid");
        assertFalse(ValidationUtils.isValidCVV("12"), "2 digits should be invalid");
        assertFalse(ValidationUtils.isValidCVV("1234"), "4 digits should be invalid");
        assertFalse(ValidationUtils.isValidCVV(null), "Null should be invalid");
    }

    @Test
    public void testIsValidPhone() {
        assertTrue(ValidationUtils.isValidPhone("03001234567"), "11 digits should be valid");
        assertFalse(ValidationUtils.isValidPhone("123"), "3 digits should be invalid");
        assertFalse(ValidationUtils.isValidPhone("abcdefghijk"), "Non-digits should be invalid");
        assertFalse(ValidationUtils.isValidPhone(null), "Null should be invalid");
    }

    @Test
    public void testIsValidCNIC() {
        assertTrue(ValidationUtils.isValidCNIC("1234512345671"), "13 digits should be valid");
        assertFalse(ValidationUtils.isValidCNIC("123"), "3 digits should be invalid");
        assertFalse(ValidationUtils.isValidCNIC(null), "Null should be invalid");
    }

    @Test
    public void testIsPositiveNumber() {
        assertTrue(ValidationUtils.isPositiveNumber("100"), "100 should be valid");
        assertTrue(ValidationUtils.isPositiveNumber("10.5"), "10.5 should be valid");
        assertFalse(ValidationUtils.isPositiveNumber("-5"), "Negative number should be invalid");
        assertFalse(ValidationUtils.isPositiveNumber("abc"), "Non-number should be invalid");
        assertFalse(ValidationUtils.isPositiveNumber(null), "Null should be invalid");
    }

    @Test
    public void testIsValidTime() {
        assertTrue(ValidationUtils.isValidTime("12:30"), "12:30 should be valid");
        assertTrue(ValidationUtils.isValidTime("00:00"), "00:00 should be valid");
        assertTrue(ValidationUtils.isValidTime("23:59"), "23:59 should be valid");
        assertFalse(ValidationUtils.isValidTime("24:00"), "24:00 should be invalid");
        assertFalse(ValidationUtils.isValidTime("12:60"), "12:60 should be invalid");
        assertFalse(ValidationUtils.isValidTime("123:12"), "Invalid format should be invalid");
        assertFalse(ValidationUtils.isValidTime(null), "Null should be invalid");
    }
}
