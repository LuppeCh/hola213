//package Main;
//
//import com.google.maps.GeoApiContext;
//import com.google.maps.PlaceAutocompleteRequest;
//import com.google.maps.PlacesApi;
//import com.google.maps.errors.ApiException;
//import com.google.maps.model.AutocompletePrediction;
//import com.google.maps.model.PlaceDetails;
//import com.google.maps.model.ComponentFilter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GooglePlacesService {
//
//    private final GeoApiContext context;
//
//    public GooglePlacesService(String apiKey) {
//        this.context = new GeoApiContext.Builder()
//                .apiKey(apiKey)
//                .build();
//    }
//
//    public String obtenerPlaceId(String texto) throws IOException, InterruptedException, ApiException {
//
//        PlaceAutocompleteRequest.SessionToken token =
//                new PlaceAutocompleteRequest.SessionToken();
//
//        AutocompletePrediction[] predictions =
//                PlacesApi.placeAutocomplete(context, texto, token).language("es").components(new ComponentFilter("country", "ar")).await();
//
//        if (predictions.length == 0) {
//            throw new IllegalArgumentException("No se encontraron coincidencias para: " + texto);
//        }
//
//        return predictions[0].placeId;
//    }
//
//    public PlaceDetails obtenerDetalles(String placeId)
//            throws IOException, InterruptedException, ApiException {
//
//        return PlacesApi
//                .placeDetails(context, placeId)
//                .language("es")
//                .await();
//    }
//
//    public List<String> autocompletar(String texto)
//            throws IOException, InterruptedException, ApiException {
//
//        PlaceAutocompleteRequest.SessionToken token =
//                new PlaceAutocompleteRequest.SessionToken();
//
//        AutocompletePrediction[] predictions = PlacesApi
//                .placeAutocomplete(context, texto, token)
//                .language("es")
//                .components(new ComponentFilter("country", "ar"))
//                .await();
//
//        List<String> resultados = new ArrayList<>();
//
//        for (AutocompletePrediction p : predictions) {
//            resultados.add(p.description);
//        }
//
//        return resultados;
//    }
//
//    public void cerrar() {
//        context.shutdown();
//    }
//}