package no.mika.taskviz;

import com.google.zxing.NotFoundException;
import no.mika.taskviz.qr.QrCodeReader;
import no.mika.taskviz.waypoints.Waypoint;
import no.mika.taskviz.waypoints.WaypointParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/waypoints")
public class WaypointsController {

    private static final Logger log = LoggerFactory.getLogger(WaypointsController.class);

    @PostMapping(path = "/asimage")
    public List<Waypoint> asImage() {
        final String HARDCODED = "/home/myuser/Downloads/this.png";

        try {
            final String decodedText = QrCodeReader.fromFile(HARDCODED);
            log.trace("Decoded QR code successfully from file: " + HARDCODED);
            return WaypointParser
                    .parse(decodedText)
                    .waypoints();
        } catch (final FileNotFoundException e) {
            log.error("Unable to find the image file: " + HARDCODED);
            return List.of();
        } catch (final IOException e) {
            log.error("Unable to read the image file: " + HARDCODED);
            return List.of();
        } catch (final NotFoundException e) {
            log.error("Unable to find an image in the file: " + HARDCODED);
            return List.of();
        }
    }
}
