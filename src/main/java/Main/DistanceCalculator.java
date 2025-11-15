package Main;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;

import java.net.URLEncoder;

public class DistanceCalculator {

    private static final String API_KEY = "AIzaSyDA7k47JKGgWgHNEAP5g3ATIGCl0NAdFKk";

    private final GeoApiContext context;

    public DistanceCalculator() {
        this.context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    public String calculateRoute(String origen, String destino) throws Exception {

        String origenContexto = origen + ", Rosario, Santa Fe, Argentina";
        String destinoContexto = destino + ", Rosario, Santa Fe, Argentina";

        String origenURL = URLEncoder.encode(origen + ", Rosario, Santa Fe", "UTF-8");
        String destinoURL = URLEncoder.encode(destino + ", Rosario, Santa Fe", "UTF-8");

        String mapLink = String.format(
                "https://www.google.com/maps/dir/%s/%s",
                origenURL,
                destinoURL
        );

        try {
            // Llama a la API de Distance Matrix
            DistanceMatrixElement element = DistanceMatrixApi.getDistanceMatrix(context,
                            new String[]{origenContexto},
                            new String[]{destinoContexto})
                    .mode(TravelMode.DRIVING)
                    .await()
                    .rows[0].elements[0];

            if (element.status.toString().equals("OK")) {
                String distancia = element.distance.humanReadable;
                String tiempo = element.duration.humanReadable;

                return String.format(
                        "RUTA ENCONTRADA:\n   - Distancia por carretera: **%s**\n   - Tiempo de viaje estimado: %s\n   -  Ver en Mapa: %s",
                        distancia, tiempo, mapLink
                );
            } else {
                return "❌ ERROR: No se pudo encontrar una ruta válida. Estado: " + element.status;
            }

        } catch (Exception e) {
            throw new Exception("Error de conexión con la API: " + e.getMessage());
        }
    }

    public void closeContext() {
        if (context != null) {
            context.shutdown();
        }
    }
}