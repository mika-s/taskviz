package no.mika.taskviz;

import static java.util.Objects.requireNonNull;

public class Waypoint {

    public final String name;
    public final double lat;
    public final double lon;

    public Waypoint(final String name, double lat, double lon) {
        this.name = requireNonNull(name);
        this.lat = lat;
        this.lon = lon;
    }

    public String name() {
        return name;
    }

    public double lat() {
        return lat;
    }

    public double lon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
               "name='" + name + '\'' +
               ", lat=" + lat +
               ", lon=" + lon +
               '}';
    }
}
