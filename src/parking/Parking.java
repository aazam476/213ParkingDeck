package parking;

/**
 * Represents a parking activity with references to the
 * {@link Vehicle} object that activity belongs to and {@link Timestamp}
 * variables that define when the vehicle entered and exited a parking deck.
 *
 * @author Layla Sampson
 */

public class Parking {
    private Vehicle vehicle;
    private Timestamp enter;
    private Timestamp exit;

    /**
     * Param constructor that sets parking's vehicle reference.
     *
     * @param vehicle Vehicle object that belongs to this activity
     */
    public Parking(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Getter method for Vehicle object
     *
     * @return Vehicle object
     */
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * Setter method for the parking activity's enter timestamp
     *
     * @param enter Timestamp object
     */
    public void setEnter(Timestamp enter) {
        this.enter = enter;
    }

    /**
     * Setter method for the parking activity's exit timestamp
     *
     * @param exit Timestamp object
     */
    public void setExit(Timestamp exit) {
        this.exit = exit;
    }

    /**
     * Getter method for the parking activity's enter timestamp
     */
    public Timestamp getEnter() {
        return this.enter;
    }

    /**
     * Getter method for the parking activity's enter timestamp
     */
    public Timestamp getExit() {
        return this.exit;
    }

    /**
     * Overrides the default toString() method for parking
     *
     * @return String representation of parking activity
     */
    @Override
    public String toString() {
        return vehicle.plate() + " [entered: " + this.enter + "]" +
                "[exited: " + this.exit + "]";
    }
}
