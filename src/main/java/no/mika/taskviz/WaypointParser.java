package no.mika.taskviz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class WaypointParser {

    public static WaypointParseResult parse(final String raw) {
        // XCTSK:{"taskType":"CLASSIC","version":2,"t":[{"n":"WPT 1","z":"cmfa@cvioJyW_X"},{"n":"WPT 2","z":"svz_@cvioJiL_X"}]}

        if (!raw.startsWith("XCTSK:")) {
            return WaypointParseResult.failure();
        }

        final String cleanedForParsing = raw.replaceFirst("XCTSK:", "");

        try {
            final TaskDefinitionFormatV2 taskDefinition = taskDefinition(cleanedForParsing);

            return WaypointParseResult.success(
                    taskDefinition.turnpoints
                            .stream()
                            .map(
                                    turnpoint -> new Waypoint(
                                            turnpoint.name,
                                            51.508, -0.11
                                            //turnpoint.coordinates
                                    )
                            )
                            .toList()
            );
        } catch (final JsonProcessingException e) {
            return WaypointParseResult.failure();
        }
    }

    private static TaskDefinitionFormatV2 taskDefinition(final String input) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(input, TaskDefinitionFormatV2.class);
    }

    public static class WaypointParseResult {
        private final boolean wasParsedOk;
        private final List<Waypoint> waypoints;

        private WaypointParseResult(final boolean wasParsedOk, final List<Waypoint> waypoints) {
            this.wasParsedOk = wasParsedOk;
            this.waypoints = waypoints;
        }

        public static WaypointParseResult failure() {
            return new WaypointParseResult(
                    false,
                    List.of()
            );
        }

        public static WaypointParseResult success(final List<Waypoint> waypoints) {
            return new WaypointParseResult(
                    true,
                    waypoints
            );
        }

        public boolean wasParsedOk() {
            return wasParsedOk;
        }

        public List<Waypoint> waypoints() {
            return waypoints;
        }

        @Override
        public String toString() {
            return "WaypointParseResult{" +
                   "wasParsedOk=" + wasParsedOk +
                   ", waypoints=" + waypoints +
                   '}';
        }
    }
}
