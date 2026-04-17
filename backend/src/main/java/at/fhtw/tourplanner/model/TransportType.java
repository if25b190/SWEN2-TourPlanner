package at.fhtw.tourplanner.model;

public enum TransportType {
    Bicycle("cyling-regular"),
    Running("foot-walking"),
    Hiking("foot-hiking");

    public final String label;

    TransportType(String label) {
        this.label = label;
    }
}
