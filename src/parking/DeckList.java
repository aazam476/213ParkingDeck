package parking;

/**
 * Represents a resizable array implementation for holding a list of
 * {@link Deck} objects. Maintains a record of both open and closed decks
 * in the Parking Management system.
 *
 * @author Layla Sampson
 */

public class DeckList {
    private static final int ARRAYLENGTH = 4;
    private static final int NOTFOUND = -1;
    private Deck[] decks;
    private int numDecks;

    /**
     * Default constructor that initializes the deck list.
     */
    public DeckList() {
        this.decks = new Deck[ARRAYLENGTH];
    }

    /**
     * Searches for the index of a specific deck in the list.
     *
     * @param deck Deck object to check for
     * @return index if found, -1 if not
     */
    private int find(Deck deck) {
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].equals(deck)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increments the length of the deck list by 4
     * once it reaches capacity.
     */
    private void grow() {
        int newLength = decks.length + 4;
        Deck[] newArray = new Deck[newLength];
        for (int i = 0; i < decks.length; i++) {
            newArray[i] = decks[i];
        }
        decks = newArray;
    }

    /**
     * Opens deck and checks if deck list is full.
     * Calls the grow() method to increase the
     * array length and increments the number
     * of decks in the list.
     *
     * @param deck the Deck object
     */
    public void open(Deck deck) {
        deck.setOpenStatus(true);
        if (decks.length == this.numDecks) {
            grow();
        }
        decks[this.numDecks] = deck;
        this.numDecks++;
    }

    /**
     * Opens already existing deck.
     *
     * @param deck Deck object
     */
    public void reopen(Deck deck) {
        deck.setOpenStatus(true);
    }

    /**
     * Calls method from Deck that handles
     * the validating of vehicles currently parked in deck,
     * which then calls its own close() method if
     * valid.
     *
     * @param deck Deck object
     */
    public void close(Deck deck) {
        deck.checkParkedVehicles();
    }

    /**
     * Boolean method that calls on find() method
     * defining earlier.
     *
     * @param deck Deck object
     * @return true if deck exists
     */
    public boolean contains(Deck deck) {
        return find(deck) != NOTFOUND;
    }

    /**
     * Sorts all decks by county name, then deck number, then prints
     * them to command-line by their toString()
     * override method.
     */
    public void printByLocation() {
        sortByCounty();
        for (int i = 0; i < numDecks; i++) {
            System.out.println(decks[i].toString());
        }
    }

    /**
     * Implements an insertion method to sort decks
     * by their county name, then calls on the sortByDeck()
     * method to complete the in-place sort.
     */
    private void sortByCounty() {
        for (int i = 1; i < this.numDecks; i++) {
            Deck key = decks[i];
            String county = decks[i].getCounty();
            int j = i - 1;
            while (j >= 0 && decks[j].getCounty().compareTo(county) > 0) {
                decks[j + 1] = decks[j];
                j--;
            }
            decks[j + 1] = key;
        }
        sortByDeck();
    }

    /**
     * Keeps track of a substring of the deck list
     * to apply an insertion sort while maintaining
     * the ordering by county name.
     */
    private void sortByDeck() {
        int start = 0;
        int end = 0;
        while (start < this.numDecks) {
            String county = decks[start].getCounty();
            while (end < this.numDecks && decks[end].getCounty().equals(county)) {
                end++;
            }

            int i = start + 1;
            while (i < end) {
                Deck key = decks[i];
                int deckNumber = decks[i].getDeckNumber();
                int j = i - 1;
                while (j >= start && decks[j].getDeckNumber() > deckNumber) {
                    decks[j + 1] = decks[j];
                    j--;
                }
                decks[j + 1] = key;
                i++;
            }

            start = end;
        }
    }

    /**
     * Prints parked vehicles in singular
     * Deck objects after sorting them by their license
     * plate number.
     *
     * @param deck Deck object
     */
    public void printVehicles(Deck deck) {
        int index = find(deck);
        sortByPlate(decks[index]);
        Parking[] p = deck.getParkings();
        for (int i = 0; i < deck.getNumParked(); i++) {
            System.out.println(p[i].toString());
        }
    }

    /**
     * Implements insertion sort to lexicographically sort the
     * license plate numbers for each vehicle registered
     * and parked in the deck.
     *
     * @param deck Deck object
     */
    private void sortByPlate(Deck deck) {
        Parking[] p = deck.getParkings();
        for (int i = 1; i < deck.getNumParked(); i++) {
            Parking key = p[i];
            String plate = key.getVehicle().plate();
            int j = i - 1;
            while (j >= 0 && p[j].getVehicle().plate().compareTo(plate) > 0) {
                p[j + 1] = p[j];
                j--;
            }
            p[j + 1] = key;
        }
    }

    /**
     * Getter method for the number of decks in the deck list,
     * both open and closed.
     *
     * @return int value of number of decks
     */
    public int getNumDecks() {
        return this.numDecks;
    }

    /**
     * Getter method for the entire list
     *
     * @return Deck[]
     */
    public Deck[] getDecks() {
        return this.decks;
    }

    /**
     * Getter method for an individual deck given
     * a deck number.
     *
     * @param deckNumber unique identifying int value
     * @return Deck object
     */
    public Deck getDeck(int deckNumber) {
        int index = NOTFOUND;
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].getDeckNumber() == deckNumber) {
                index = i;
                break;
            }
        }
        return index == NOTFOUND ? null : decks[index];
    }
}

