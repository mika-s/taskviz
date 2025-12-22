package no.mika.taskviz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static no.mika.taskviz.util.Collectors.zipWithIndex;

public class WaypointParser {

    public static WaypointParseResult parse(final String raw) {
        if (!raw.startsWith("XCTSK:")) {
            return WaypointParseResult.failure();
        }

        final String cleanedForParsing = raw.replaceFirst("XCTSK:", "");

        try {
            final TaskDefinitionFormatV2 taskDefinition = taskDefinition(cleanedForParsing);

            final List<LatLonAltRadius> coordinates = PolylineAlgorithm.decodeToDecimalDegrees(
                    taskDefinition.turnpoints
                            .stream()
                            .map(turnpoint -> turnpoint.coordinates)
                            .collect(joining(""))
            );

            return WaypointParseResult.success(
                    zipWithIndex(taskDefinition.turnpoints.stream())
                            .map(
                                    turnpointAndIndex -> turnpointAndIndex
                                            .map((turnpoint, index) ->
                                                    new Waypoint(
                                                            turnpoint.name,
                                                            coordinates.get(index)
                                                    )
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
