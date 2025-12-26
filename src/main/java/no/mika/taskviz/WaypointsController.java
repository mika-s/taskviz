package no.mika.taskviz;

import com.google.zxing.NotFoundException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import no.mika.taskviz.qr.QrCodeReader;
import no.mika.taskviz.waypoints.Waypoint;
import no.mika.taskviz.waypoints.WaypointParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/waypoints")
public class WaypointsController {

    private static final Logger log = LoggerFactory.getLogger(WaypointsController.class);

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

    @PostMapping(path = "/asimage")
    public ResponseEntity<List<Waypoint>> asImage(@RequestParam("imageFile") final MultipartFile file) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        if (file.isEmpty()) {
            log.trace("Submitted empty file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        try {
            final String decodedText = QrCodeReader.fromInputStream(file.getInputStream());
            log.trace("Decoded QR code successfully from file: {}", file.getOriginalFilename());
            return ResponseEntity.ok(
                    WaypointParser
                            .parse(decodedText)
                            .waypoints()
            );
        } catch (final FileNotFoundException e) {
            log.error("Unable to find the image file: {}", file.getOriginalFilename());
            return ResponseEntity.badRequest().build();
        } catch (final IOException e) {
            log.error("Unable to read the image file: {}", file.getOriginalFilename());
            return ResponseEntity.badRequest().build();
        } catch (final NotFoundException e) {
            log.error("Unable to find an QR code in the image: {}", file.getOriginalFilename());
            return ResponseEntity.badRequest().build();
        }
    }
}
