package no.mika.taskviz;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import no.mika.taskviz.waypoints.Waypoint;
import no.mika.taskviz.waypoints.WaypointParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/waypoints")
public class WaypointsController {

    private final Bucket bucket;

    public WaypointsController() {
        this.bucket = Bucket.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(20)
                                .refillGreedy(20, Duration.ofMinutes(1))
                                .build()
                )
                .build();
    }

    @PostMapping(path = "/astext")
    public ResponseEntity<List<Waypoint>> asText(@RequestBody final InputDto input) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        return ResponseEntity.ok(
                WaypointParser
                        .parse(input.decodedQr())
                        .waypoints()
        );
    }
}
