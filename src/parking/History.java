package parking;

/**
 * A node in a singly linked list that stores one completed parking
 * activity for a {@link Vehicle}. Each {@code History} node wraps a
 * {@link Parking} object and a reference to the next node in the
 * list, so a vehicle's entire parking history can be represented as
 * a chain of nodes starting from the vehicle's head reference.
 *
 * @author Ali Azam
 */
public class History {

    /** The completed parking activity stored in this node. */
    private Parking parking;

    /**
     * Reference to the next node in the list, or null if this is
     * the last node.
     */
    private History next;

    /**
     * Constructs a history node wrapping the given parking activity.
     * The node's next reference starts out as null until it is
     * linked into a list with {@link #setNext(History)}.
     *
     * @param parking the completed parking activity to store
     */
    public History(Parking parking) {
        this.parking = parking;
        this.next = null;
    }

    /**
     * Returns the parking activity stored in this node.
     *
     * @return the parking activity stored in this node
     */
    public Parking parking() {
        return parking;
    }

    /**
     * Returns the next node in the linked list.
     *
     * @return the next node, or null if this is the last node
     */
    public History next() {
        return next;
    }

    /**
     * Links this node to the next node in the list.
     *
     * @param next the node that should follow this node
     */
    public void setNext(History next) {
        this.next = next;
    }
}
