package Main;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;

import java.net.URLEncoder;

public class DistanceCalculator {

    private static final String API_KEY = "AIzaSyB6uynr_3ELge4l5JrkDNGh3JYs-zO53DI";

    private final GeoApiContext context;

    public DistanceCalculator() {
        this.context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    public Ruta calculateRoute(String origen, String destino) throws Exception {

        String origenContexto = origen + ", Rosario, Santa Fe, Argentina";
        String destinoContexto = destino + ", Rosario, Santa Fe, Argentina";

        String origenURL = URLEncoder.encode(origen + ", Rosario, Santa Fe", "UTF-8");
        String destinoURL = URLEncoder.encode(destino + ", Rosario, Santa Fe", "UTF-8");

        String mapLink = String.format(
                "https://www.google.com/maps/dir/%s/%s",
                origenURL,
                destinoURL
        );

        // Llama a la API de Distance Matrix
        DistanceMatrixElement element = DistanceMatrixApi.getDistanceMatrix(context,
                        new String[]{origenContexto},
                        new String[]{destinoContexto})
                .mode(TravelMode.DRIVING)
                .await()
                .rows[0].elements[0];

        if (!element.status.toString().equals("OK")) {
            throw new Exception("No se pudo obtener la ruta. Estado: " + element.status);
        }

        return new Ruta(
                element.distance.inMeters,
                element.distance.humanReadable,
                element.duration.humanReadable,
                mapLink
        );
    }

    public void closeContext() {
        if (context != null) {
            context.shutdown();
        }
    }
}