package parking;

/**
 * A resizable-array based list of {@link Vehicle} objects registered
 * with the Parking Management System. The backing array starts with
 * a capacity of {@value #LENGTH} and grows by {@value #LENGTH} slots
 * whenever it becomes full; its capacity never shrinks. Sorting is
 * performed in place with insertion sort so that no additional
 * array and no Java Collections utilities are required.
 *
 * @author Ali Azam
 */
public class VehicleList {

    /**
     * Initial array capacity, and the number of slots added each
     * time the array grows.
     */
    private static final int LENGTH = 4;

    /**
     * Sentinel value returned by {@link #find(Vehicle)} when a
     * vehicle is not present.
     */
    private static final int NOTFOUND = -1;

    /** Backing array holding the registered vehicles. */
    private Vehicle[] vehicles;

    /** Number of vehicles currently stored in the array. */
    private int numVehicles;

    /**
     * Constructs an empty vehicle list with an initial capacity of
     * {@value #LENGTH}.
     */
    public VehicleList() {
        vehicles = new Vehicle[LENGTH];
        numVehicles = 0;
    }

    /**
     * Searches the array for a vehicle equal to the given vehicle.
     * Equality is based on {@link Vehicle#equals(Object)}, which
     * compares license plates.
     *
     * @param vehicle the vehicle to search for
     * @return the index of the match, or {@value #NOTFOUND} if
     *         not found
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < numVehicles; i++) {
            if (vehicles[i].equals(vehicle)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Grows the backing array by {@value #LENGTH} slots, copying
     * over all vehicles currently stored. The array's capacity
     * never shrinks.
     */
    private void grow() {
        Vehicle[] newVehicles = new Vehicle[vehicles.length + LENGTH];
        for (int i = 0; i < vehicles.length; i++) {
            newVehicles[i] = vehicles[i];
        }
        vehicles = newVehicles;
    }

    /**
     * Sorts the vehicles currently stored in the array by license
     * plate, in ascending order, using an in-place insertion sort.
     */
    private void sortByPlate() {
        for (int i = 1; i < numVehicles; i++) {
            Vehicle key = vehicles[i];
            int position = i - 1;
            while (position >= 0
                    && vehicles[position].plate()
                            .compareTo(key.plate()) > 0) {
                vehicles[position + 1] = vehicles[position];
                position--;
            }
            vehicles[position + 1] = key;
        }
    }

    /**
     * Adds a vehicle to the end of the array, growing the array
     * first if it is already full.
     *
     * @param vehicle the vehicle to add
     */
    public void add(Vehicle vehicle) {
        if (numVehicles == vehicles.length) {
            grow();
        }
        vehicles[numVehicles] = vehicle;
        numVehicles++;
    }

    /**
     * Removes a vehicle from the array, if present, by overwriting
     * its slot with the last vehicle in the array and decrementing
     * the count of stored vehicles. Does nothing if the vehicle is
     * not found in the list.
     *
     * @param vehicle the vehicle to remove
     */
    public void remove(Vehicle vehicle) {
        int index = find(vehicle);
        if (index == NOTFOUND) {
            return;
        }
        vehicles[index] = vehicles[numVehicles - 1];
        vehicles[numVehicles - 1] = null;
        numVehicles--;
    }

    /**
     * Returns whether a vehicle equal to the given vehicle is
     * present in the list.
     *
     * @param vehicle the vehicle to search for
     * @return true if an equal vehicle is found
     */
    public boolean contains(Vehicle vehicle) {
        return find(vehicle) != NOTFOUND;
    }

    /**
     * Returns whether the list has no registered vehicles, so the user
     * interface can report an empty list instead of printing nothing.
     *
     * @return true if no vehicles are registered
     */
    public boolean isEmpty() {
        return numVehicles == 0;
    }

    /**
     * Looks up the registered vehicle with the given license plate.
     * This overloads {@link #find(Vehicle)} so that a caller who
     * only has a plate string (for example, from user input) can
     * still retrieve the actual stored {@link Vehicle} object.
     *
     * @param plate the license plate to search for
     * @return the matching vehicle, or null if no registered
     *         vehicle has that plate
     */
    public Vehicle find(String plate) {
        for (int i = 0; i < numVehicles; i++) {
            if (vehicles[i].plate().equalsIgnoreCase(plate)) {
                return vehicles[i];
            }
        }
        return null;
    }

    /**
     * Prints the list of registered vehicles to the terminal,
     * ordered by license plate in ascending order.
     */
    public void printByPlate() {
        sortByPlate();
        for (int i = 0; i < numVehicles; i++) {
            System.out.println(vehicles[i].toString());
        }
    }

    /**
     * Prints the parking history of every registered vehicle to the
     * terminal. Vehicles are ordered by license plate in ascending
     * order; each vehicle's own activities are ordered by entry
     * timestamp in descending order.
     */
    public void printHistory() {
        sortByPlate();
        for (int i = 0; i < numVehicles; i++) {
            if (vehicles[i].hasHistory()) {
                vehicles[i].printHistory();
            }
        }
    }
}
