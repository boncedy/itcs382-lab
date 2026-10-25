package bikeshare;

public class Bike {
    private final String id;
    private String station;

    public Bike(String id, String station) {
        this.id = id;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public String getStation() {
        return station;
    }

    void returnTo(String station) {
        this.station = station;
    }
}
