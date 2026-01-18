package railwaymanagementsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import railwaymanagementsystem.utils.FareCalculator;

public class FareCalculatorTest {

    @Test
    public void testEconomyFare() {
        double basePrice = 1000.0;
        int seats = 2;
        // Economy should be base * seats * 1
        double expected = 2000.0;
        assertEquals(expected, FareCalculator.calculateTotalFare(basePrice, seats, "Economy"), 0.01);
    }

    @Test
    public void testACStandardFare() {
        double basePrice = 1000.0;
        int seats = 2;
        // AC Standard should be base * seats * 1.5
        double expected = 3000.0;
        assertEquals(expected, FareCalculator.calculateTotalFare(basePrice, seats, "AC Standard"), 0.01);
    }

    @Test
    public void testACBusinessFare() {
        double basePrice = 1000.0;
        int seats = 2;
        // AC Business should be base * seats * 2.0
        double expected = 4000.0;
        assertEquals(expected, FareCalculator.calculateTotalFare(basePrice, seats, "AC Business"), 0.01);
    }

    @Test
    public void testInvalidInputs() {
        assertEquals(0.0, FareCalculator.calculateTotalFare(-100, 1, "Economy"), 0.01);
        assertEquals(0.0, FareCalculator.calculateTotalFare(100, 0, "Economy"), 0.01);
    }
    
    @Test
    public void testCaseInsensitivity() {
        double basePrice = 1000.0;
        int seats = 1;
        double expected = 2000.0;
        assertEquals(expected, FareCalculator.calculateTotalFare(basePrice, seats, "ac business"), 0.01);
    }
}
