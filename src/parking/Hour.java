package parking;

public enum Hour {
    HR5 ("5:00", "21:30"),
    HR6 ("6:30", "18:30"),
    HR7 ("7:00", "18:00");

    private final String startTime;
    private final String endTime;

    Hour(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStart() {
        return this.startTime;
    }

    public String getEnd() { return this.endTime; }

    @Override
    public String toString() {
        return this.startTime + " ~ " + this.endTime;
    }
}
