package no.mika.taskviz.waypoints;

import no.mika.taskviz.util.Tuple2;

import java.util.ArrayList;
import java.util.List;

import static no.mika.taskviz.util.Tuple.tuple;

/// [Description](https://developers.google.com/maps/documentation/utilities/polylinealgorithm)
class PolylineAlgorithm {

    static List<LatLonAltRadius> decodeToDecimalDegrees(final String encoded) {
        final int len = encoded.length();
        final List<LatLonAltRadius> path = new ArrayList<>();
        int index = 0;

        while (index < len) {
            final Tuple2<Integer, Integer> latRet = decode(encoded, index);
            final int lat = latRet._1();
            index = latRet._2();

            final Tuple2<Integer, Integer> lonRet = decode(encoded, index);
            final int lon = lonRet._1();
            index = lonRet._2();

            final Tuple2<Integer, Integer> altRet = decode(encoded, index);
            final int altitude = altRet._1();
            index = altRet._2();

            final Tuple2<Integer, Integer> radRet = decode(encoded, index);
            final int radius = radRet._1();
            index = radRet._2();

            path.add(new LatLonAltRadius(lon * 1e-5, lat * 1e-5, altitude, radius));
        }

        return path;
    }

    private static Tuple2<Integer, Integer> decode(final String encoded, final int initialIndex) {
        int index = initialIndex;
        int b;
        int result = 1;
        int shift = 0;

        do {
            b = encoded.charAt(index++) - 63 - 1;
            result += b << shift;
            shift += 5;
        } while (b >= 0x1f);

        return tuple(
                ((result & 1) != 0) ? ~(result >> 1) : (result >> 1),
                index
        );
    }
}
