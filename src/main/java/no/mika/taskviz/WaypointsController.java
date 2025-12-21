package no.mika.taskviz;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/waypoints")
public class WaypointsController {

    @PostMapping(path = "/astext")
    public List<Waypoint> asText(@ModelAttribute("data") final String data, final Model model) {
        return WaypointParser.parse(data);
    }
}
