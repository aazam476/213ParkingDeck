package parking;

public enum Location {
    BRIDGEWATER ("Somerset", "08807"),
    PISCATAWAY ("Middlesex", "08854"),
    EDISON ("Middlesex", "08817"),
    PRINCETON ("Mercer", "08542"),
    MORRISTOWN ("Morris", "07960"),
    CLARK ("Union", "07066");

    private final String county;
    private final String zipcode;

    Location(String county, String zipcode) {
        this.county = county;
        this.zipcode = zipcode;
    }

    public String getCounty() {
        return this.county;
    }
}
