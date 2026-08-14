import java.util.*;

// ==========================================
// IMMUTABLE PRODUCT CLASS WITH INNER BUILDER
// ==========================================

public class FlightSearchQuery {
    // Mandatory attributes
    private final String source;
    private final String destination;
    private final String departureDate;

    // Optional attributes (default values provided)
    private final int maxPriceInRupees;
    private final boolean isNonStopOnly;
    private final String preferredAirline; // e.g., IndiGo, Air India
    private final String cabinClass;       // e.g., Economy, Business
    private final int passengerCount;

    // Private constructor: Can ONLY be instantiated by the Builder!
    private FlightSearchQuery(Builder builder) {
        this.source = builder.source;
        this.destination = builder.destination;
        this.departureDate = builder.departureDate;
        this.maxPriceInRupees = builder.maxPriceInRupees;
        this.isNonStopOnly = builder.isNonStopOnly;
        this.preferredAirline = builder.preferredAirline;
        this.cabinClass = builder.cabinClass;
        this.passengerCount = builder.passengerCount;
    }

    // Getters ONLY (No Setters -> Fully Immutable!)
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureDate() { return departureDate; }
    public int getMaxPriceInRupees() { return maxPriceInRupees; }
    public boolean isNonStopOnly() { return isNonStopOnly; }
    public String getPreferredAirline() { return preferredAirline; }
    public String getCabinClass() { return cabinClass; }
    public int getPassengerCount() { return passengerCount; }

    @Override
    public String toString() {
        return "FlightSearchQuery [" +
                "From=" + source +
                ", To=" + destination +
                ", Date=" + departureDate +
                ", MaxPrice=₹" + (maxPriceInRupees > 0 ? maxPriceInRupees : "Any") +
                ", NonStopOnly=" + isNonStopOnly +
                ", Airline=" + (preferredAirline != null ? preferredAirline : "Any") +
                ", Class=" + cabinClass +
                ", Passengers=" + passengerCount +
                ']';
    }

    // ==========================================
    // STATIC INNER BUILDER CLASS
    // ==========================================

    public static class Builder {
        // Mandatory fields
        private final String source;
        private final String destination;
        private final String departureDate;

        // Optional fields (initialized to safe defaults)
        private int maxPriceInRupees = -1;
        private boolean isNonStopOnly = false;
        private String preferredAirline = null;
        private String cabinClass = "Economy";
        private int passengerCount = 1;

        // Builder Constructor enforcing MANDATORY fields
        public Builder(String source, String destination, String departureDate) {
            if (source == null || destination == null || departureDate == null) {
                throw new IllegalArgumentException("Source, Destination, and Date are MANDATORY!");
            }
            this.source = source;
            this.destination = destination;
            this.departureDate = departureDate;
        }

        // Fluent chaining setter methods (returning 'this')
        public Builder withMaxPrice(int maxPriceInRupees) {
            this.maxPriceInRupees = maxPriceInRupees;
            return this;
        }

        public Builder setNonStopOnly(boolean isNonStopOnly) {
            this.isNonStopOnly = isNonStopOnly;
            return this;
        }

        public Builder withPreferredAirline(String preferredAirline) {
            this.preferredAirline = preferredAirline;
            return this;
        }

        public Builder withCabinClass(String cabinClass) {
            this.cabinClass = cabinClass;
            return this;
        }

        public Builder withPassengerCount(int passengerCount) {
            this.passengerCount = passengerCount;
            return this;
        }

        // Final step: Build and return the immutable Product
        public FlightSearchQuery build() {
            // Business Validation before object creation!
            if (source.equalsIgnoreCase(destination)) {
                throw new IllegalStateException("Source and Destination cannot be the same city!");
            }
            if (passengerCount <= 0) {
                throw new IllegalStateException("Passenger count must be at least 1!");
            }
            return new FlightSearchQuery(this);
        }
    }
}

// ==========================================
// INTERVIEW TEST RUNNER (MAIN CLASS)
// ==========================================

class Main {
    public static void main(String[] args) {
        System.out.println(">>> MAKEMYTRIP FLIGHT SEARCH QUERY BUILDER <<<\n");

        // Search 1: Simple Search (Only Mandatory Parameters)
        FlightSearchQuery basicSearch = new FlightSearchQuery.Builder("DELHI", "MUMBAI", "2026-09-15")
                .build();
        System.out.println("Basic Search: " + basicSearch);

        // Search 2: Advanced Search with Filters (Fluent Method Chaining)
        FlightSearchQuery filterSearch = new FlightSearchQuery.Builder("BENGALURU", "DELHI", "2026-10-01")
                .withMaxPrice(8500)
                .setNonStopOnly(true)
                .withPreferredAirline("IndiGo")
                .withCabinClass("Business")
                .withPassengerCount(2)
                .build();
        System.out.println("\nFiltered Search: " + filterSearch);

        // Search 3: Validation Error Example
        try {
            System.out.println("\nTesting Validation Error...");
            FlightSearchQuery invalidSearch = new FlightSearchQuery.Builder("DELHI", "DELHI", "2026-09-15")
                    .build();
        } catch (Exception e) {
            System.out.println("❌ Validation Caught: " + e.getMessage());
        }
    }
}

/*
Console Output:
code
Text
>>> MAKEMYTRIP FLIGHT SEARCH QUERY BUILDER <<<

Basic Search: FlightSearchQuery [From=DELHI, To=MUMBAI, Date=2026-09-15, MaxPrice=Any, NonStopOnly=false, Airline=Any, Class=Economy, Passengers=1]

Filtered Search: FlightSearchQuery [From=BENGALURU, To=DELHI, Date=2026-10-01, MaxPrice=₹8500, NonStopOnly=true, Airline=IndiGo, Class=Business, Passengers=2]

Testing Validation Error...
❌ Validation Caught: Source and Destination cannot be the same city!
*/