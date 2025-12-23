package no.mika.taskviz;

import com.google.zxing.NotFoundException;
import no.mika.taskviz.qr.QrCodeReader;
import no.mika.taskviz.waypoints.Waypoint;
import no.mika.taskviz.waypoints.WaypointParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/waypoints")
public class WaypointsController {

    private static final Logger log = LoggerFactory.getLogger(WaypointsController.class);

    @PostMapping(path = "/asimage")
    public List<Waypoint> asImage(@RequestParam("imageFile") final MultipartFile file) {
        if (file.isEmpty()) {
            log.trace("Submitted empty file: {}", file.getOriginalFilename());
            return List.of();
        }

        try {
            final String decodedText = QrCodeReader.fromInputStream(file.getInputStream());
            log.trace("Decoded QR code successfully from file: {}", file.getOriginalFilename());
            return WaypointParser
                    .parse(decodedText)
                    .waypoints();
        } catch (final FileNotFoundException e) {
            log.error("Unable to find the image file: {}", file.getOriginalFilename());
            return List.of();
        } catch (final IOException e) {
            log.error("Unable to read the image file: {}", file.getOriginalFilename());
            return List.of();
        } catch (final NotFoundException e) {
            log.error("Unable to find an QR code in the image: {}", file.getOriginalFilename());
            return List.of();
        }
    }
}
