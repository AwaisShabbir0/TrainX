package railwaymanagementsystem.utils;

public class FareCalculator {

    public static final double MULTIPLIER_BC = 2.0;
    public static final double MULTIPLIER_ACC = 1.5;
    
    /**
     * Calculates the total fare for a booking.
     * 
     * @param basePrice The base price of the ticket (from DB).
     * @param numberOfSeats The number of seats booked.
     * @param className The class of travel ("AC Business", "AC Standard", "Economy").
     * @return The total calculated fare.
     */
    public static double calculateTotalFare(double basePrice, int numberOfSeats, String className) {
        if (basePrice < 0 || numberOfSeats <= 0) {
            return 0.0;
        }

        double multiplier = 1.0;

        if ("AC Business".equalsIgnoreCase(className)) {
            multiplier = MULTIPLIER_BC;
        } else if ("AC Standard".equalsIgnoreCase(className)) {
            multiplier = MULTIPLIER_ACC;
        }

        return basePrice * numberOfSeats * multiplier;
    }
}
