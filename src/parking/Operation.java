package parking;
import java.util.Scanner;

/**
 * The user interface class for processing single or
 * multi-line command line arguments. Terminates upon commmand
 * 'Q' being entered in command-line.
 *
 * @author Layla Sampson
 */

public class Operation {
    private DeckList deckList;
    private VehicleList vehicleList;
    private final String SEPARATOR = "\\s+";

    /**
     * Default constructor for Operation class.
     * Initializes object references to the DeckList and VehicleList.
     */
    public Operation() {
        this.deckList = new DeckList();
        this.vehicleList = new VehicleList();
    }

    /**
     * Entry-point method for executing commands.
     * Checks for valid commands and passes the remaining string tokens
     * to the appropriate helper methods.
     */
    public void run() {
        System.out.println("Parking Management System is in operation.");
        Scanner scan = new Scanner(System.in);
        String command = "";
        while (!command.equals("Q")) {
            command = scan.nextLine();
            int commandLength = command.length();
            if (commandLength == 0) { continue; }
            switch (command.charAt(0)) {
                case 'A':
                    System.out.println(commandA(command.substring(2, commandLength))); break;
                case 'R':
                    System.out.println(commandR(command.substring(2, commandLength))); break;
                case 'O':
                    System.out.println(commandO(command.substring(2, commandLength))); break;
                case 'C':
                    System.out.println(commandC(command.substring(2, commandLength))); break;
                case 'E':
                    System.out.println(commandE(command.substring(2, commandLength))); break;
                case 'X':
                    System.out.println(commandX(command.substring(2, commandLength))); break;
                case 'P':
                    commandP(command); break;
                case 'Q':
                    System.out.println("Parking Management System is terminated."); break;
                default:
                    System.out.println(command + " is an invalid command!");
            }
        }
        scan.close();
    }

    /**
     * Registers and adds vehicles to the vehicle list if given
     * a valid license plate and/or vehicle is not already
     * registered in the system.
     *
     * @param tokens a substring containing the license plate to be checked
     */
    private String commandA(String tokens) {
        boolean isValid = Vehicle.isValidPlate(tokens);
        if (isValid) {
            if (isRegistered(tokens)) {
                return tokens + " is already registered.";
            } else {
                Vehicle vehicle = new Vehicle(tokens);
                vehicleList.add(vehicle);
                return tokens + " registered.";
            }
        }
        else {
            return tokens + " - invalid license plate format.";
        }
    }

    /**
     * Unregisters vehicles and removes them from the Vehicle list if
     * they exist, don't have parking history, and aren't currently
     * parked in a deck.
     *
     * @param tokens a substring containing the license plate to be checked
     * @return String to be printed on command-line
     */

    private String commandR(String tokens) {
        boolean isValid = Vehicle.isValidPlate(tokens);
        if (isValid) {
            if (!isRegistered(tokens)) {
                return tokens + " does not exist; cannot be unregistered.";
            }
            if (isParked(tokens)) {
                return "Cannot be unregistered - " + tokens + " is in a deck.";
            }
            if (hasHistory(tokens)) {
                return "Cannot be unregistered - " + tokens + " has parking history.";
            }
            Vehicle vehicle = vehicleList.find(tokens);
            vehicleList.remove(vehicle);
            return tokens + " unregistered.";
        }
        else {
            return tokens + " - invalid license plate format.";
        }
    }

    /**
     * Helper method for checking a vehicle's registration
     * in the system.
     *
     * @param plate String object representing license plate
     * @return true if vehicle is registered in system
     */
    private boolean isRegistered(String plate) {
        return vehicleList.find(plate) != null;
    }

    /**
     * Helper method for checking if vehicle with given
     * license plate has entered a parking deck
     * and not exited yet.
     *
     * @param plate String object representing license plate
     * @return true if vehicle is parked in a deck
     */

    private boolean isParked(String plate) {
        Vehicle vehicle = vehicleList.find(plate);
        Deck[] decks = deckList.getDecks();
        for (int i = 0; i < deckList.getNumDecks(); i++) {
            Parking[] p = decks[i].getParkings();
            int j = 0;
            while (j < decks[i].getNumParked()) {
                if (p[j].getVehicle().equals(vehicle)) {
                    return true;
                }
                j++;
            }
        }
        return false;
    }

    /**
     * Helper method that calls the hasHistory() method
     * from the Vehicle class.
     *
     * @param plate String object representing license plate
     * @return true if vehicle has history
     */
    private boolean hasHistory(String plate) {
        return vehicleList.find(plate).hasHistory();
    }

    /**
     * Helper method for checking if token contains
     * no characters other than integer values.
     *
     * @param num String object that should be representing a numeric
     * @return true if String matches the regex
     */
    private boolean isNumeric(String num) {
        String intRegex = "^[0-9]+$";
        return num.trim().matches(intRegex);
    }

    /**
     * Opens a new deck or re-opens a deck that was previously
     * closed if given a valid deck number, sufficient amount of tokens,
     * and accurate location/hour information.
     *
     * @param tokens a substring containing remaining tokens
     * @return String to be printed on command-line
     */

    private String commandO(String tokens) {
        String[] deckInfo = tokens.split(SEPARATOR);

        if (!isNumeric(deckInfo[0])) {
            return deckInfo[0] + " - invalid deck number.";
        }

        Deck deck = deckList.getDeck(Integer.parseInt(deckInfo[0]));
        if (deck != null) {
            return reopenDeck(deck);
        }

        if (deckInfo.length < 4) {
            return "Error opening Deck#" + deckInfo[0] + " - missing data tokens.";
        } else {
            if (!isNumeric(deckInfo[3])) { return deckInfo[3] + " - invalid capacity; it's not an integer."; }
            if (Integer.parseInt(deckInfo[3]) > 6) { return deckInfo[3] + " - exceeds the maximum deck capacity 6"; }
            if (!isValidLocation(deckInfo[1])) { return deckInfo[1] + " - invalid location."; }
            if (!areValidHours(deckInfo[2])) { return deckInfo[2] + " - invalid operation hours"; }

            return openDeck(deckInfo);
        }
    }


    /**
     * Helper method that handles re-opening a pre-existing
     * deck.
     *
     * @param deck Deck object retrieved from deckList
     * @return String to be printed to command-line
     */
    private String reopenDeck(Deck deck) {
        if (!deck.isOpen()) {
            deckList.reopen(deck);
            return "Deck#" + deck.getDeckNumber() + " - was closed, now reopened.";
        }
        return "Deck#" + deck.getDeckNumber() + " - is already open.";
    }


    /**
     * Helper method that handles opening new decks.
     *
     * @param deckTokens String array containing remaining tokens
     * @return String to be printed to command-line
     */
    private String openDeck(String[] deckTokens) {
        int deckNumber = Integer.parseInt(deckTokens[0]);
        Location location = Location.valueOf(deckTokens[1].toUpperCase());
        Hour hours = Hour.valueOf(deckTokens[2].toUpperCase());
        int capacity = Integer.parseInt(deckTokens[3]);

        Deck deck = new Deck(deckNumber, capacity, location, hours);
        deckList.open(deck);

        return "Deck#" + deckNumber + " opened.";
    }

    /**
     * Helper method for checking if 3-character operating hours
     * are values of the Hour enum class.
     *
     * @param hours 3-character string
     * @return true if String matches with one of Hour's values
     */
    private boolean areValidHours(String hours) {
        for (Hour hour : Hour.values()) {
            if (hour.name().equalsIgnoreCase(hours)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for validating location token against
     * Location enum class values.
     *
     * @param location String containing location to be checked
     * @return true if String matches one of Location's values
     */
    private boolean isValidLocation(String location) {
        for (Location loc: Location.values()) {
            if (loc.name().equalsIgnoreCase(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Closes deck if it exists in deckList and is currently
     * open.
     *
     * @param tokens String containing remaining tokens
     * @return String to be printed on command-line
     */
    private String commandC(String tokens) {
        if (!isNumeric(tokens)) {
            return tokens + " - invalid deck number; " +
                    "it contains characters.";
        }

        int deckNumber = Integer.parseInt(tokens.trim());
        Deck deck = deckList.getDeck(deckNumber);
        if (deckList.getDeck(deckNumber) == null) {
           return "Deck#" + deckNumber + " - does not exist.";
        }

        if (!deck.isOpen()) {
            return "Deck#" + deckNumber + " - is already closed.";
        }

        deckList.close(deck);

        return "Deck#" + deckNumber + " - closed.";
    }


    /**
     * Allows vehicle objects to enter an open deck if
     * vehicle isn't already parked in another deck,
     * license plate is valid, and the deck isn't at capacity.
     *
     * @param tokens String containing remaining tokens
     * @return String to be printed on command-line
     */
    private String commandE(String tokens) {
        String[] vehicleInfo = tokens.split(SEPARATOR);
        if (!isNumeric(vehicleInfo[0])) {
            return vehicleInfo[0] + " - invalid deck number; " +
                    "it contains characters.";
        }
        int deckNumber = Integer.parseInt(vehicleInfo[0]);
        Deck deck = deckList.getDeck(deckNumber);
        if (deck == null) {
            return "Deck#" + deckNumber + " - does not exist.";
        } else {
            if (deck.getNumParked() == deck.getCapacity()) {
                return "Deck#" + deckNumber + " - is full.";
            }
            if (!deck.isOpen()) {
                return "Deck#" + deckNumber + " - is closed for parking.";
            }
        }
        if (!validateVehicle(vehicleInfo[1]).isEmpty()) {
            return validateVehicle(vehicleInfo[1]);
        }
        if (!validateEnterDateTime(deck, vehicleInfo[2], vehicleInfo[3]).isEmpty()) {
            return validateEnterDateTime(deck, vehicleInfo[2], vehicleInfo[3]);
        }

        Vehicle vehicle = vehicleList.find(vehicleInfo[1]);
        Parking parking = new Parking(vehicle);
        Date enterDate = getDate(vehicleInfo[2]);
        Timestamp entered = new Timestamp(enterDate, vehicleInfo[3]);
        parking.setEnter(entered);
        deck.enter(parking);
        return vehicleInfo[1] + " entered Deck#" + deckNumber  +
                " on " + entered.toString();
    }

    /**
     * Helper method for initializing a Date object.
     *
     * @param tokens String containing year, month, and day
     * @return a new Date object
     */
    private Date getDate(String tokens) {
        String[] dateUnits = tokens.split("-");
        int year = Integer.parseInt(dateUnits[0]);
        int month = Integer.parseInt(dateUnits[1]);
        int day = Integer.parseInt(dateUnits[2]);
        return new Date(year, month, day);
    }

    /**
     * Validates all vehicle information for the E command. If vehicle is
     * not registered, the license plate is invalid, or the vehicle is already
     * parked in a deck, the method will return a String to be printed to the
     * command-line.
     *
     * @param enterTokens String containing tokens related to the vehicle
     * @return "" if the vehicle passes all validation
     */
    private String validateVehicle(String enterTokens) {
        if (!Vehicle.isValidPlate(enterTokens)) {
            return enterTokens + " - invalid license plate format.";
        } else {
            if (!isRegistered(enterTokens)) {
                return enterTokens + " - is not registered.";
            }
            if (isParked(enterTokens)) {
                return "Error entering - " + enterTokens +
                        " is already in a deck.";
            }
        }
        return "";
    }

    /**
     * Validates if date, hour, and minutes are valid dates and times.
     * Will return a String statement to be printed to the command-line
     * if any three of those are invalid. Calls a second method to check the
     * Timestamp object against the deck's operating hours.
     *
     * @param deck the Deck object passed to the second method
     * @param enterDate String to be converted to a Date object
     * @param enterTime String to be broken into hours and minutes
     * @return "" if all validation is passed
     */
    private String validateEnterDateTime(Deck deck, String enterDate, String enterTime) {
        Date date = getDate(enterDate);
        if (!date.isValid()) {
            return enterDate + " - invalid calendar date.";
        }

        String[] timeUnits = enterTime.split(":");
        int hour = Integer.parseInt(timeUnits[0]);
        int minute = Integer.parseInt(timeUnits[1]);
        if (hour < 0 || hour > 23) {
            return hour + " - invalid hour.";
        }
        if (minute < 0 || minute > 59) {
            return minute + " - invalid minute.";
        }

        Timestamp timestamp = new Timestamp(date, hour, minute);
        if (!validateEnterHours(deck, timestamp).isEmpty()) {
            return validateEnterHours(deck, timestamp);
        }

        return "";
    }

    /**
     * Validates that the vehicle's enter timestamp is within
     * the chosen deck's operating hours. Will return a String to be printed
     * to the command-line if not.
     *
     * @param deck Deck object to retrieve operating hours from
     * @param timestamp the initialized timestamp
     * @return "" if timestamp is within the operating hours
     */
    private String validateEnterHours(Deck deck, Timestamp timestamp) {
        String[] startTime = deck.getStartHours().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMinute = Integer.parseInt(startTime[1]);

        String[] endTime = deck.getEndHours().split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMinute = Integer.parseInt(endTime[1]);

        Date date = timestamp.getDate();
        Timestamp startTimestamp = new Timestamp(date, startHour, startMinute);
        Timestamp endTimestamp = new Timestamp(date, endHour, endMinute);

        if (!(timestamp.compareTo(startTimestamp) >= 0 && timestamp.compareTo(endTimestamp) <= 0)) {
            return "Error entering - not within the operating hours: " + deck.getHour().toString();
        }
        return "";
    }


    /**
     * Allows a vehicle to exit if the license plate is valid,
     * the vehicle is currently parked in a deck, and
     * the exit time is greater than the enter time but not more
     * than two days.
     *
     * @param tokens String containing remaining tokens for validation
     * @return String to be printed to the commmand-line
     */
    private String commandX (String tokens) {
        String[] vehicleInfo = tokens.split(SEPARATOR);

        if (!Vehicle.isValidPlate(vehicleInfo[0])) {
            return vehicleInfo[0] + " - invalid license plate format.";
        }

        String plate = vehicleInfo[0];
        if (findDeck(plate) == null) {
            return "Error exiting - " + plate + " is not in a deck.";
        }

        Deck deck = findDeck(plate);
        String exitDate = vehicleInfo[1];
        String exitTime = vehicleInfo[2];
        if (!validateExitDateTime(deck, exitDate, exitTime, plate).isEmpty()) {
            return validateExitDateTime(deck, exitDate, exitTime, plate);
        }

        Vehicle vehicle = vehicleList.find(plate);
        Parking parking = new Parking(vehicle);
        Date date = getDate(exitDate);
        Timestamp exited = new Timestamp(date, exitTime);
        parking.setExit(exited);
        deck.exit(parking);
        vehicle.addHistory(parking);
        return plate + " exited Deck#" + deck.getDeckNumber() +
                " on " + exited.toString();
    }

    /**
     * Helper method for finding the deck object the vehicle
     * belongs to.
     *
     * @param plate String representation of the vehicle's license plate
     * @return Deck object if the vehicle is parked
     */
    private Deck findDeck(String plate) {
        Vehicle vehicle = vehicleList.find(plate);
        Deck[] decks = deckList.getDecks();
        for (int i = 0; i < deckList.getNumDecks(); i++) {
            Parking[] p = decks[i].getParkings();
            int j = 0;
            while (j < decks[i].getNumParked()) {
                if (p[j].getVehicle().equals(vehicle)) {
                    return decks[i];
                }
                j++;
            }
        }
        return null;
    }


    /**
     * Validates the date and time given, ensuring
     * the date is a valid calendar date and that the
     * hour/minute are within the correct range. Calls
     * a second method that validates the exit timestamp
     * against the deck's operating hours.
     *
     * @param deck Deck object to pass to second method
     * @param exitDate String representation of the date
     * @param exitTime String representation of the time
     * @param plate String representation of the vehicle's license plate
     * @return "" if validation checks pass
     */
    private String validateExitDateTime(Deck deck, String exitDate, String exitTime, String plate) {
        Date date = getDate(exitDate);
        if (!date.isValid()) {
            return exitDate + " - invalid calendar date.";
        }

        String[] timeUnits = exitTime.split(":");
        int hour = Integer.parseInt(timeUnits[0]);
        int minute = Integer.parseInt(timeUnits[1]);
        if (hour < 0 || hour > 23) {
            return hour + " - invalid hour.";
        }
        if (minute < 0 || minute > 59) {
            return minute + " - invalid minute.";
        }

        Timestamp timestamp = new Timestamp(date, hour, minute);
        if (!validateExitHours(deck, timestamp, plate).isEmpty()) {
            return validateExitHours(deck, timestamp, plate);
        }

        return "";
    }

    /**
     * Validates that the given time is within the deck's operating hours,
     * greater than the vehicle's entered time, and doesn't exceed two days
     * from when the vehicle entered the deck.
     *
     * @param deck Deck object to retrieve operating hours from
     * @param timestamp Timestamp object to validate
     * @param plate String representation of the license plate
     * @return "" if all validation passes
     */
    private String validateExitHours(Deck deck, Timestamp timestamp, String plate) {
        String[] startTime = deck.getStartHours().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMinute = Integer.parseInt(startTime[1]);

        String[] endTime = deck.getEndHours().split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMinute = Integer.parseInt(endTime[1]);

        Date date = timestamp.getDate();
        Timestamp startTimestamp = new Timestamp(date, startHour, startMinute);
        Timestamp endTimestamp = new Timestamp(date, endHour, endMinute);

        if (!(timestamp.compareTo(startTimestamp) >= 0 && timestamp.compareTo(endTimestamp) <= 0)) {
            return "Error exiting - not within the operating hours: " + deck.getHour().toString();
        }

        Vehicle vehicle = vehicleList.find(plate);
        Parking parking = deck.getParking(vehicle);
        Timestamp entered = parking.getEnter();

        if (timestamp.compareTo(entered) < 0) {
            return "Exiting time " + timestamp.toString() +
                    " before entering time " + entered.toString();
        }

        if (date.getDay() - entered.getDate().getDay() > 2) {
            return "Invalid exiting time - exceeds two days.";
        }

        return "";
    }

    /**
     * Entry-point for all three P commands, checking the second character
     * and calling the appropriate method.
     *
     * @param tokens contains remaining String token on command-line
     */
    private void commandP(String tokens) {
        if (tokens.charAt(1) == 'P') {
            displayVehicles();
        }
        if (tokens.charAt(1) == 'D') {
            displayDecks(tokens);
        }
        if (tokens.charAt(1) == 'H') {
            displayHistory(tokens);
        }
    }


    /**
     * Prints the entire list of vehicles registered in the system.
     */
    private void displayVehicles() {
        if (vehicleList.isEmpty()) {
            System.out.println("Vehicle list is empty - no vehicle is registered.");
        } else {
            System.out.println("** List of registered vehicles, ordered by license plate **");
            vehicleList.printByPlate();
            System.out.println("** end of list **");
        }

    }

    /**
     * Prints the full deck list, ordered by county then deck number, if
     * not given a deck number. Prints vehicles parked in a Deck object, ordered
     * by license plate, for a given deck number passed in command-line.
     *
     * @param tokens String containing remaining tokens
     */
    private void displayDecks(String tokens) {
        String[] deckTokens = tokens.split(SEPARATOR);
        if (deckTokens.length == 2) {
            if (!isNumeric(deckTokens[1])) {
                System.out.println(deckTokens[1] + " - invalid deck number; " +
                        "it contains characters.");
                return;
            }

            int deckNumber = Integer.parseInt(deckTokens[1]);
            Deck deck = deckList.getDeck(deckNumber);

            if (deck != null && deck.isOpen()) {
                System.out.println("** List of vehicles in Deck# " + deckNumber
                        + ", ordered by plate **");
                deckList.printVehicles(deck);
                System.out.println("** end of list **");
            }
        } else {
            if (deckList.getNumDecks() == 0) {
                System.out.println("Deck list is empty - no deck is open.");
            } else {
                System.out.println("** List of decks, ordered by county/deck number **");
                deckList.printByLocation();
                System.out.println("** end of list **");
            }

        }
    }

    /**
     * Prints entire parking history, ordered by plate then timestamps,
     * if no license plate is provided. Prints parking history
     * for individual Vehicle objects if license plate
     * is passed to command-line.
     *
     * @param tokens String containing remaining tokens
     */
    private void displayHistory(String tokens) {
        String[] vehicleTokens = tokens.split(SEPARATOR);
        if (vehicleTokens.length == 2) {
          if (!Vehicle.isValidPlate(vehicleTokens[1])) {
              System.out.println(vehicleTokens[1] + " - invalid license plate format.");
              return;
          }
          String plate = vehicleTokens[1];
          Vehicle vehicle = vehicleList.find(plate);
          if (vehicle != null) {
              if (vehicle.hasHistory()) {
                  System.out.println("** Parking history for " + plate + "**");
                  vehicle.printHistory();
                  System.out.println("** end of list **");
              } else {
                  System.out.println(plate + " - no parking history.");
              }
          } else {
              System.out.println(plate + " does not exist.");
          }
        } else {
            if (deckList.getNumDecks() == 0) {
                System.out.println("Deck list is empty - no deck is open.");
            } else {
                System.out.println("** Parking history for all vehicles, " +
                        "ordered by plate/timestamp **");
                vehicleList.printHistory();
                System.out.println("** end of parking history **");
            }
        }

    }
}
