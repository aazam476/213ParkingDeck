package parking;

/**
 * Enum class representing the acceptable locations for
 * a deck to be opened in, with additional county and
 * zipcode attributes.
 *
 * @author Layla Sampson
 */
public enum Location {
    BRIDGEWATER("Somerset", "08807"),
    PISCATAWAY("Middlesex", "08854"),
    EDISON("Middlesex", "08817"),
    PRINCETON("Mercer", "08542"),
    MORRISTOWN("Morris", "07960"),
    CLARK("Union", "07066");

    private final String county;
    private final String zipcode;

    /**
     * Constructor that defines the county and zipcode
     * for each location.
     *
     * @param county String object representing county
     * @param zipcode String object representing zipcode
     */
    Location(String county, String zipcode) {
        this.county = county;
        this.zipcode = zipcode;
    }

    /**
     * Getter method for retreiving a location's county.
     *
     * @return String object representing county
     */
    public String getCounty() {
        return this.county;
    }
}
