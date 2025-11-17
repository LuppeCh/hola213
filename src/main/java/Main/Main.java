package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

    private WebEngine engine;
    private String apiKey = "AIzaSyDA7k47JKGgWgHNEAP5g3ATIGCl0NAdFKk";  // Cambiar luego

    @Override
    public void start(Stage stage) {

        //-------------------------------------
        // UI SUPERIOR
        //-------------------------------------
        Label lblOrigen = new Label("Origen:");
        TextField txtOrigen = new TextField();
        txtOrigen.setPrefWidth(200);

        Label lblDestino = new Label("Destino:");
        TextField txtDestino = new TextField();
        txtDestino.setPrefWidth(200);

        Button btnBuscar = new Button("Calcular Ruta");

        HBox topBar = new HBox(10, lblOrigen, txtOrigen, lblDestino, txtDestino, btnBuscar);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);

        //-------------------------------------
        // MAP VIEW
        //-------------------------------------
        WebView webView = new WebView();
        engine = webView.getEngine();

        // Cargar mapa con Google Maps JavaScript API
        engine.loadContent(generarHTML(apiKey));

        //-------------------------------------
        // BOTÓN: CALCULAR RUTA
        //-------------------------------------
        btnBuscar.setOnAction(event -> {
            String origen = txtOrigen.getText();
            String destino = txtDestino.getText();

            if (!origen.isBlank() && !destino.isBlank()) {
                String comando = String.format("calcularRuta('%s','%s')",
                        origen.replace("'", "\\'"),
                        destino.replace("'", "\\'"));

                engine.executeScript(comando);
            }
        });

        //-------------------------------------
        // LAYOUT
        //-------------------------------------
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Maps – JavaFX UI");
        stage.show();
    }

    //-------------------------------------
    // HTML + Javascript para Google Maps
    //-------------------------------------
    private String generarHTML(String apiKey) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="initial-scale=1, width=device-width">
            <style>
                html, body { height: 100%; margin: 0; padding: 0; }
                #map { height: 100%; width: 100%; }
            </style>
        """ +
                "<script src=\"https://maps.googleapis.com/maps/api/js?key=" + apiKey + "&libraries=places\"></script>" +
                """
                <script>
                    let map, directionsService, directionsRenderer;
        
                    function initMap() {
                        directionsService = new google.maps.DirectionsService();
                        directionsRenderer = new google.maps.DirectionsRenderer();
        
                        map = new google.maps.Map(document.getElementById('map'), {
                            zoom: 12,
                            center: { lat: -34.6037, lng: -58.3816 }
                        });
        
                        directionsRenderer.setMap(map);
                    }
        
                    function calcularRuta(origen, destino) {
                        const request = {
                            origin: origen,
                            destination: destino,
                            travelMode: 'DRIVING'
                        };
        
                        directionsService.route(request, function(result, status) {
                            if (status === 'OK') {
                                directionsRenderer.setDirections(result);
                            } else {
                                alert("No se pudo calcular la ruta: " + status);
                            }
                        });
                    }
        
                    window.onload = initMap;
                </script>
                </head>
                <body>
                    <div id="map"></div>
                </body>
                </html>
                """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}