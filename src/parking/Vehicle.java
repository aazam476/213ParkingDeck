package parking;

/**
 * Represents a single vehicle registered with the Parking Management
 * System. A vehicle is uniquely identified by its license plate and
 * keeps its own completed parking activities as a singly linked list
 * of {@link History} nodes, with the most recently completed
 * activity stored at the head of the list.
 *
 * @author Ali Azam
 */
public class Vehicle {

    /** Number of characters in a valid license plate, e.g. "R48-JIK". */
    private static final int PLATE_LENGTH = 7;

    /** Index of the required dash character in a license plate. */
    private static final int DASH_INDEX = 3;

    /**
     * This vehicle's license plate; exactly 7 characters in the
     * "Xdd-XXX" format.
     */
    private String plate;

    /**
     * Head reference of the singly linked list of this vehicle's
     * completed parking activities.
     */
    private History history;

    /**
     * Constructs a vehicle with the given license plate. The plate
     * is stored in uppercase because license plates are not case
     * sensitive, and the vehicle starts out with no parking history.
     *
     * @param plate the license plate to associate with this vehicle
     */
    public Vehicle(String plate) {
        this.plate = plate.toUpperCase();
        this.history = null;
    }

    /**
     * Checks whether a license plate string has the required
     * format: exactly 7 characters, "Xdd-XXX", where each X is an
     * alphabetic letter and each d is a digit (for example,
     * "R48-JIK"). The check is not case sensitive.
     *
     * @param plate the license plate string to validate
     * @return true if the plate matches the required format
     */
    public static boolean isValidPlate(String plate) {
        if (plate == null || plate.length() != PLATE_LENGTH) {
            return false;
        }
        if (!Character.isLetter(plate.charAt(0))) {
            return false;
        }
        for (int i = 1; i < DASH_INDEX; i++) {
            if (!Character.isDigit(plate.charAt(i))) {
                return false;
            }
        }
        if (plate.charAt(DASH_INDEX) != '-') {
            return false;
        }
        for (int i = DASH_INDEX + 1; i < PLATE_LENGTH; i++) {
            if (!Character.isLetter(plate.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns this vehicle's license plate.
     *
     * @return this vehicle's license plate
     */
    public String plate() {
        return plate;
    }

    /**
     * Returns whether this vehicle has completed at least one
     * parking activity in the past.
     *
     * @return true if this vehicle has parking history
     */
    public boolean hasHistory() {
        return history != null;
    }

    /**
     * Records a completed parking activity by adding it to the
     * front of this vehicle's parking history. Parking activities
     * are processed in chronological order, so inserting each newly
     * completed activity at the head of the list keeps the list
     * ordered by entry timestamp in descending order, without
     * needing to sort it.
     *
     * @param parking the completed parking activity to record
     */
    public void addHistory(Parking parking) {
        History node = new History(parking);
        node.setNext(history);
        history = node;
    }

    /**
     * Prints this vehicle's parking history to the terminal, one
     * completed parking activity per line, ordered by entry
     * timestamp in descending order (most recent activity first).
     */
    public void printHistory() {
        History current = history;
        while (current != null) {
            System.out.println(current.parking().toString());
            current = current.next();
        }
    }

    /**
     * Returns true if the given object is a Vehicle with the same
     * license plate as this vehicle, and false otherwise.
     *
     * @param obj the object to compare against this vehicle
     * @return true if obj is a Vehicle with an equal license plate
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) obj;
        return this.plate.equals(other.plate);
    }

    /**
     * Returns a textual representation of this vehicle, which is
     * its license plate.
     *
     * @return this vehicle's license plate
     */
    @Override
    public String toString() {
        return plate;
    }
}
