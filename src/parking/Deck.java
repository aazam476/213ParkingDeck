package parking;

/**
 * Represents a single parking deck in the system that can be
 * uniquely identified with a deck number. Holds a list reference
 * of current parking activities.
 *
 * @author Layla Sampson
 */

public class Deck {
    public static final int MAXCAPACITY = 6;
    private int number;
    private Location location;
    private Hour hour;
    private Parking[] parkings;
    private int numParked;
    private boolean open;

    /**
     * Param constructor for Deck that initializes its parkings array
     *
     * @param number deck number
     * @param capacity maximum number of vehicles that can park
     * @param location enum value from Location
     * @param hour enum value from Hour
     */
    public Deck(int number, int capacity, Location location, Hour hour) {
        this.number = number;
        this.location = location;
        this.hour = hour;
        parkings = new Parking[capacity];
        this.open = true;
    }

    /**
     * Returns index of specific Vehicle object
     * parked in the deck.
     *
     * @param vehicle Vehicle object of interest
     * @return int index of vehicle in parkings array
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < this.numParked; i++) {
            if (parkings[i].getVehicle().equals(vehicle)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds parking object to array as long as deck
     * is under or equal to its capacity. Increments
     * the number of vehicles parked.
     *
     * @param parking Parking object representing parking activity
     */
    public void enter(Parking parking) {
        if (this.numParked < parkings.length) {
            parkings[this.numParked] = parking;
            this.numParked++;
        }

    }

    /**
     * Removes parking activity from array by setting it to null
     * and shifting the rest of the elements to the left and
     * decrementing the number of vehicles parked.
     *
     * @param parking Parking object representing parking activity
     */
    public void exit(Parking parking) {
        int vehicleIndex = find(parking.getVehicle());
        if (vehicleIndex != -1)  {
            parkings[vehicleIndex] = null;
            int validIndex = 0;
            for (int i = 0; i < this.numParked; i++) {
                if (parkings[i] != null) {
                    parkings[validIndex] = parkings[i];
                    validIndex++;
                }
            }
            for (int i = validIndex; i < this.numParked; i++) {
                parkings[i] = null;
            }
            this.numParked--;
        }
    }

    /**
     * Checks if deck is currently open
     *
     * @return true if deck is open
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Sets the deck object's open status to
     * the boolean passed
     *
     * @param status true if setting to open, false if setting to closed
     */
    public void setOpenStatus(boolean status) {
        this.open = status;
    }

    /**
     * Get the deck's unique number
     *
     * @return int value of deck's number
     */
    public int getDeckNumber() {
        return this.number;
    }

    /**
     * Get the number of vehicles parked in deck
     *
     * @return int value of vehicles parked
     */
    public int getNumParked() {
        return this.numParked;
    }

    /**
     * Get the maximum-set capacity for this deck
     *
     * @return int value of capacity
     */
    public int getCapacity() { return parkings.length; }

    /**
     * Get the county the deck's location belongs to
     *
     * @return String representation of the county
     */
    public String getCounty() { return this.location.getCounty(); }

    /**
     * Get the Hour object belonging to the deck
     *
     * @return Hour representation of the 3-character timeframe
     */
    public Hour getHour() { return this.hour; }

    /**
     * Get the deck's starting hours
     *
     * @return String representation of initial operating hours
     */
    public String getStartHours() { return this.hour.getStart(); }

    /**
     * Get the deck's closing hours
     *
     * @return String represnation of closed operating hours
     */
    public String getEndHours() { return this.hour.getEnd(); }

    /**
     * Check if the deck currently has any vehicles parked.
     * If not, then the open status may be changed to false.
     */
    public void checkParkedVehicles() {
        boolean hasActiveParking = false;
        for (int i = 0; i < this.numParked; i++) {
            if (parkings[i].getExit() == null) {
                hasActiveParking = true;
                break;
            }
        }
        if (!hasActiveParking) {
            setOpenStatus(false);
        }
    }

    /**
     * Get the array reference to parking activities
     *
     * @return Parking[]
     */
    public Parking[] getParkings() {
        return parkings;
    }

    /**
     * Get specific parking activity given a Vehicle object.
     *
     * @param vehicle Vehicle object that belongs to the activity
     * @return Parking object
     */
    public Parking getParking(Vehicle vehicle) {
        int index = find(vehicle);
        if (index != -1) {
            return parkings[index];
        }
        return null;
    }

    /**
     * Overrides the equals() method to check if two deck objects
     * have the same deck number
     *
     * @param obj   the reference object with which to compare.
     * @return true if deck numbers are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Deck) {
            Deck deck = (Deck) obj;
            return deck.number == this.number;
        }
        return false;
    }

    /**
     * Overrding the toString() method to display
     * all deck information on command-line
     *
     * @return String representation of Deck object
     */
    @Override
    public String toString() {
        String identifier = "vehicles";
        if (this.numParked == 1) {
            identifier = "vehicle";
        }
        return String.format("Deck#%d@%s[open %s ~ %s] [capacity %d] [%d %s] [%s]",
                this.number, location.name(), hour.getStart(), hour.getEnd(), parkings.length, this.numParked, identifier,
                location.getCounty());
    }

}
