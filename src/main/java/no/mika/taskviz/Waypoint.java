package no.mika.taskviz;

import static java.util.Objects.requireNonNull;

public class Waypoint {

    public final String name;
    public final double lat;
    public final double lon;
    public final double altitude;
    public final double radius;

    public Waypoint(final String name, final LatLonAltRadius latLonAltRadius) {
        this.name = requireNonNull(name);
        requireNonNull(latLonAltRadius);
        this.lat = latLonAltRadius.lat();
        this.lon = latLonAltRadius.lon();
        this.altitude = latLonAltRadius.altitude();
        this.radius = latLonAltRadius.radius();
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

    public double altitude() {
        return altitude;
    }

    public double radius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
               "name='" + name + '\'' +
               ", lat=" + lat +
               ", lon=" + lon +
               ", altitude=" + altitude +
               ", radius=" + radius +
               '}';
    }
}
