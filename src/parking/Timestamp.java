package parking;

/**
 * A point in time for a parking activity, made up of a calendar date and
 * an hour and minute in 24-hour format.
 * A Timestamp can check whether it is valid and compare itself to other
 * timestamps chronologically.
 *
 * @author Ali Azam
 */
public class Timestamp implements Comparable<Timestamp> {
    /** The earliest valid hour in 24-hour format. */
    private static final int MIN_HOUR = 0;

    /** The latest valid hour in 24-hour format. */
    private static final int MAX_HOUR = 23;

    /** The earliest valid minute. */
    private static final int MIN_MINUTE = 0;

    /** The latest valid minute. */
    private static final int MAX_MINUTE = 59;

    /** The character between the hour and minute in a time string. */
    private static final String SEPARATOR = ":";

    /** The number of fields in a time string: hour and minute. */
    private static final int FIELD_COUNT = 2;

    /** The position of the hour in a time string. */
    private static final int HOUR_FIELD = 0;

    /** The position of the minute in a time string. */
    private static final int MINUTE_FIELD = 1;

    /**
     * Stored for an hour or minute that cannot be read or does not fit in
     * a byte. It is below every valid value, so such a timestamp is never
     * valid.
     */
    private static final byte INVALID = -1;

    /** The calendar date of this timestamp. */
    private Date date;

    /** The hour of this timestamp, in 24-hour format (0-23). */
    private byte hour;

    /** The minute of this timestamp (0-59). */
    private byte minute;

    /**
     * Constructs a Timestamp on the given date from a time string in
     * 24-hour "HH:mm" form, such as "10:48". A string that is not two
     * whole numbers separated by a colon gives a timestamp that
     * {@code isValid()} reports as invalid.
     *
     * @param date the calendar date
     * @param time the time string to read
     */
    public Timestamp(Date date, String time) {
        int parsedHour = INVALID;
        int parsedMinute = INVALID;
        String[] fields = time.split(SEPARATOR);
        if (fields.length == FIELD_COUNT) {
            try {
                parsedHour = Integer.parseInt(fields[HOUR_FIELD]);
                parsedMinute = Integer.parseInt(fields[MINUTE_FIELD]);
            } catch (NumberFormatException exception) {
                parsedHour = INVALID;
                parsedMinute = INVALID;
            }
        }
        this.date = date;
        this.hour = toByte(parsedHour);
        this.minute = toByte(parsedMinute);
    }

    /**
     * Constructs a Timestamp with the given date, hour, and minute.
     * Does not validate the timestamp on construction; use
     * {@code isValid()} to check whether the resulting timestamp is
     * a valid 24-hour time on a valid calendar date.
     *
     * @param date   the calendar date
     * @param hour   the hour, in 24-hour format
     * @param minute the minute
     */
    public Timestamp(Date date, int hour, int minute) {
        this.date = date;
        this.hour = toByte(hour);
        this.minute = toByte(minute);
    }

    /**
     * Converts an hour or minute to a byte for storage. A value too large
     * or too small for a byte is stored as INVALID, instead of wrapping
     * around to a different number that could pass {@code isValid()}.
     *
     * @param value the hour or minute to store
     * @return the value as a byte, or INVALID if it does not fit
     */
    private static byte toByte(int value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            return INVALID;
        }
        return (byte) value;
    }

    /**
     * Returns the date of this timestamp.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the hour of this timestamp.
     *
     * @return the hour, in 24-hour format
     */
    public byte getHour() {
        return hour;
    }

    /**
     * Returns the minute of this timestamp.
     *
     * @return the minute
     */
    public byte getMinute() {
        return minute;
    }

    /**
     * Checks whether the date is a valid calendar date and the hour
     * and minute form a valid 24-hour time.
     *
     * @return {@code true} if the timestamp is valid,
     *         {@code false} otherwise
     */
    public boolean isValid() {
        if (!date.isValid()) {
            return false;
        }
        return hour >= MIN_HOUR && hour <= MAX_HOUR
                && minute >= MIN_MINUTE && minute <= MAX_MINUTE;
    }

    /**
     * Compares this timestamp to another chronologically, first by
     * date, then by hour, then by minute.
     *
     * @param other the timestamp to compare against
     * @return a negative integer, zero, or a positive integer as
     *         this timestamp is earlier than, equal to, or later
     *         than {@code other}
     */
    @Override
    public int compareTo(Timestamp other) {
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) {
            return dateCompare;
        }
        int hourCompare = Integer.compare(this.hour, other.hour);
        if (hourCompare != 0) {
            return hourCompare;
        }
        return Integer.compare(this.minute, other.minute);
    }

    /**
     * Returns a string representation of this timestamp in
     * "yyyy-MM-dd HH:mm" form.
     *
     * @return the formatted timestamp string
     */
    @Override
    public String toString() {
        return date.toString() + " "
                + String.format("%02d:%02d", hour, minute);
    }

    /**
     * Tests the {@code compareTo()} method with cases that return a
     * negative value, a positive value, and zero.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Date early = new Date("2025-12-31");
        Date base = new Date("2026-03-09");
        Date late = new Date("2026-03-10");

        Timestamp reference = new Timestamp(base, "10:48");

        Timestamp earlierDate = new Timestamp(early, "23:59");
        Timestamp earlierHour = new Timestamp(base, "09:59");
        Timestamp earlierMinute = new Timestamp(base, "10:30");

        System.out.println("Test 1 - earlier date: "
                + "expected -1, actual " + earlierDate.compareTo(reference));
        System.out.println("Test 2 - same date, earlier hour: "
                + "expected -1, actual " + earlierHour.compareTo(reference));
        System.out.println("Test 3 - same date/hour, earlier minute: "
                + "expected -1, actual "
                + earlierMinute.compareTo(reference));

        Timestamp laterDate = new Timestamp(late, "00:00");
        Timestamp laterHour = new Timestamp(base, "11:00");
        Timestamp laterMinute = new Timestamp(base, "10:50");

        System.out.println("Test 4 - later date: "
                + "expected 1, actual " + laterDate.compareTo(reference));
        System.out.println("Test 5 - same date, later hour: "
                + "expected 1, actual " + laterHour.compareTo(reference));
        System.out.println("Test 6 - same date/hour, later minute: "
                + "expected 1, actual " + laterMinute.compareTo(reference));

        Timestamp equalTimestamp = new Timestamp(base, "10:48");
        System.out.println("Test 7 - identical timestamp: "
                + "expected 0, actual "
                + equalTimestamp.compareTo(reference));
    }
}
