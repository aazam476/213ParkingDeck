package parking;

/**
 * Enum class that represents the 3-character codes for the
 * three valid operating hour time frames for {@link Deck} objects.
 *
 * @author Layla Sampson
 */
public enum Hour {
    HR5("5:00", "21:30"),
    HR6("6:30", "18:30"),
    HR7("7:00", "18:00");

    private final String startTime;
    private final String endTime;

    /**
     * Constructor that defines the start and close
     * hours of operation for a deck as String objects.
     *
     * @param startTime String object representing when deck opens
     * @param endTime   String object representing when deck closes
     */
    Hour(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Getter method for the deck's start time.
     *
     * @return String object representing when deck opens
     */
    public String getStart() {
        return this.startTime;
    }

    /**
     * Getter method for the deck's close time.
     *
     * @return String object representing when deck closes
     */
    public String getEnd() {
        return this.endTime;
    }

    /**
     * Overrides the default toString() to display the deck's
     * opening and closing hours when called in
     * print statement defined in {@link DeckList}
     *
     * @return String object representing operating hours
     */
    @Override
    public String toString() {
        return this.startTime + " ~ " + this.endTime;
    }
}
