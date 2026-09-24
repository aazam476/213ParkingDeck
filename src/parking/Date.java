package parking;

import java.util.Calendar;

/**
 * A calendar date made up of a year, month, and day, used as part of the
 * timestamp of a parking activity.
 * A Date can be created from numbers or from a "yyyy-MM-dd" string. It
 * can check whether it is a valid calendar date, compare itself to other
 * dates, and format itself as a readable string.
 *
 * @author Ali Azam
 */
public class Date implements Comparable<Date> {
    /** The number of January, the first month of the year. */
    private static final int JAN = Calendar.JANUARY + 1;

    /** The number of February, whose length depends on the year. */
    private static final int FEB = Calendar.FEBRUARY + 1;

    /** The number of April, which has 30 days. */
    private static final int APR = Calendar.APRIL + 1;

    /** The number of June, which has 30 days. */
    private static final int JUN = Calendar.JUNE + 1;

    /** The number of September, which has 30 days. */
    private static final int SEP = Calendar.SEPTEMBER + 1;

    /** The number of November, which has 30 days. */
    private static final int NOV = Calendar.NOVEMBER + 1;

    /** The number of December, the last month of the year. */
    private static final int DEC = Calendar.DECEMBER + 1;

    /** Divisor used in the first step of the leap year rule. */
    private static final int QUADRENNIAL = 4;

    /** Divisor used in the second step of the leap year rule. */
    private static final int CENTENNIAL = 100;

    /** Divisor used in the third step of the leap year rule. */
    private static final int QUATERCENTENNIAL = 400;

    /** The first day of every month. */
    private static final int FIRST_DAY = 1;

    /**
     * The number of days in January, March, May, July, August, October,
     * and December.
     */
    private static final int DAYS_IN_LONG_MONTH = 31;

    /** The number of days in April, June, September, and November. */
    private static final int DAYS_IN_SHORT_MONTH = 30;

    /** The number of days in February in a non-leap year. */
    private static final int DAYS_IN_FEB = 28;

    /** The number of days in February in a leap year. */
    private static final int DAYS_IN_FEB_LEAP = 29;

    /** The character between the year, month, and day in a date string. */
    private static final String SEPARATOR = "-";

    /** The number of fields in a date string: year, month, and day. */
    private static final int FIELD_COUNT = 3;

    /** The position of the year in a date string. */
    private static final int YEAR_FIELD = 0;

    /** The position of the month in a date string. */
    private static final int MONTH_FIELD = 1;

    /** The position of the day in a date string. */
    private static final int DAY_FIELD = 2;

    /**
     * Stored in every field when a date string cannot be read. No month
     * is numbered 0, so such a date is never valid.
     */
    private static final int INVALID = 0;

    /** The year of this date. */
    private int year;

    /** The month of this date (1-12). */
    private int month;

    /** The day of this date (1-31, depending on month and year). */
    private int day;

    /**
     * Constructs a Date from a string in "yyyy-MM-dd" form, such as
     * "2026-10-09". A string that is not three whole numbers separated
     * by dashes gives a date that {@code isValid()} reports as invalid.
     *
     * @param date the date string to read
     */
    public Date(String date) {
        int parsedYear = INVALID;
        int parsedMonth = INVALID;
        int parsedDay = INVALID;
        String[] fields = date.split(SEPARATOR);
        if (fields.length == FIELD_COUNT) {
            try {
                parsedYear = Integer.parseInt(fields[YEAR_FIELD]);
                parsedMonth = Integer.parseInt(fields[MONTH_FIELD]);
                parsedDay = Integer.parseInt(fields[DAY_FIELD]);
            } catch (NumberFormatException exception) {
                parsedYear = INVALID;
                parsedMonth = INVALID;
                parsedDay = INVALID;
            }
        }
        this.year = parsedYear;
        this.month = parsedMonth;
        this.day = parsedDay;
    }

    /**
     * Constructs a Date with the given year, month, and day.
     * Does not validate the date on construction; use {@code isValid()}
     * to check whether the resulting date is a valid calendar date.
     *
     * @param year  the year
     * @param month the month (1-12)
     * @param day   the day of the month
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Determines whether the year of this date is a leap year, following
     * the standard four-step divisibility rule.
     *
     * @return {@code true} if the year is a leap year,
     *         {@code false} otherwise
     */
    private boolean isLeapYear() {
        return (year % QUADRENNIAL == 0 && year % CENTENNIAL != 0)
                || year % QUATERCENTENNIAL == 0;
    }

    /**
     * Returns the number of days in the month of this date, counting
     * February as 29 days in a leap year and 28 days otherwise.
     *
     * @return the number of days in this date's month
     */
    private int daysInMonth() {
        if (month == FEB) {
            return isLeapYear() ? DAYS_IN_FEB_LEAP : DAYS_IN_FEB;
        }
        if (month == APR || month == JUN || month == SEP || month == NOV) {
            return DAYS_IN_SHORT_MONTH;
        }
        return DAYS_IN_LONG_MONTH;
    }

    /**
     * Returns the year of this date.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the month of this date.
     *
     * @return the month, from 1 to 12
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day of this date.
     *
     * @return the day of the month
     */
    public int getDay() {
        return day;
    }

    /**
     * Checks whether the stored year, month, and day form a valid
     * calendar date, accounting for varying month lengths and leap years.
     *
     * @return {@code true} if the date is valid, {@code false} otherwise
     */
    public boolean isValid() {
        if (month < JAN || month > DEC) {
            return false;
        }
        return day >= FIRST_DAY && day <= daysInMonth();
    }

    /**
     * Compares this date to another date chronologically.
     *
     * @param other the date to compare against
     * @return a negative integer, zero, or a positive integer as this
     *         date is earlier than, equal to, or later than {@code other}
     */
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    /**
     * Compares this date to another object for equality.
     *
     * @param obj the object to compare against
     * @return {@code true} if {@code obj} is a {@code Date} with the
     *         same year, month, and day, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Date)) {
            return false;
        }
        Date other = (Date) obj;
        return year == other.year && month == other.month && day == other.day;
    }

    /**
     * Returns a string representation of this date in yyyy-MM-dd form.
     *
     * @return the formatted date string
     */
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Tests the {@code isValid()} method with four invalid and two
     * valid calendar dates.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Date monthTooHigh = new Date("2026-13-01");
        Date dayTooLow = new Date("2026-04-00");
        Date febNonLeap = new Date("2019-02-29");
        Date aprilThirtyOne = new Date("2026-04-31");

        System.out.println("Test 1 - month out of range: "
                + "expected false, actual " + monthTooHigh.isValid());
        System.out.println("Test 2 - day out of range: "
                + "expected false, actual " + dayTooLow.isValid());
        System.out.println("Test 3 - Feb 29 in a non-leap year: "
                + "expected false, actual " + febNonLeap.isValid());
        System.out.println("Test 4 - April 31 (April has 30 days): "
                + "expected false, actual " + aprilThirtyOne.isValid());

        Date normalDate = new Date("2026-10-09");
        Date febLeapCentennial = new Date("2000-02-29");

        System.out.println("Test 5 - ordinary valid date: "
                + "expected true, actual " + normalDate.isValid());
        System.out.println("Test 6 - Feb 29 in a century leap year: "
                + "expected true, actual " + febLeapCentennial.isValid());
    }
}
