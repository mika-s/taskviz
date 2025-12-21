package no.mika.taskviz;

import java.util.List;

public class WaypointParser {

    public static List<Waypoint> parse(final String raw) {
        return List.of(
                new Waypoint("WPT 1", 51.508, -0.11),
                new Waypoint("WPT 2", 51.408, -0.11)
        );
    }
}
