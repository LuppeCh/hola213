package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AppUI extends Application {

    private GooglePlacesService places;
    private DistanceCalculator calculator;

    @Override
    public void start(Stage stage) {

        // Inicializar servicios
        places = new GooglePlacesService("AIzaSyDA7k47JKGgWgHNEAP5g3ATIGCl0NAdFKk");
        calculator = new DistanceCalculator();

        // --- UI ELEMENTOS ---
        Label lblOrigen = new Label("Origen:");
        TextField txtOrigen = new TextField();
        txtOrigen.setPromptText("Ingresar origen...");

        Label lblDestino = new Label("Destino:");
        TextField txtDestino = new TextField();
        txtDestino.setPromptText("Ingresar destino...");

        Button btnCalcular = new Button("Calcular Ruta");

        TextArea areaResultado = new TextArea();
        areaResultado.setEditable(false);
        areaResultado.setPrefHeight(180);

        // --- ACCIÓN DEL BOTÓN ---
        btnCalcular.setOnAction(e -> {
            try {
                String origen = pedirDireccion(txtOrigen.getText());
                String destino = pedirDireccion(txtDestino.getText());

                Ruta ruta = calculator.calculateRoute(origen, destino);

                areaResultado.setText(
                        "✔ RUTA ENCONTRADA\n\n" +
                                "Distancia: " + ruta.distanciaTexto() + "\n" +
                                "Duración: " + ruta.tiempoTexto() + "\n" +
                                "Mapa: " + ruta.linkMapa()
                );

            } catch (Exception ex) {
                areaResultado.setText("❌ ERROR: " + ex.getMessage());
            }
        });

        // --- LAYOUT ---
        VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                lblOrigen, txtOrigen,
                lblDestino, txtDestino,
                btnCalcular,
                areaResultado
        );

        // --- ESCENA ---
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Calculador de Rutas");
        stage.setScene(scene);
        stage.show();
    }

    // -------------------------------------------------------
    // MÉTODO REUTILIZABLE PARA OBTENER DIRECCIÓN COMPLETA
    // -------------------------------------------------------
    private String pedirDireccion(String texto) throws Exception {

        // Paso 1 → Autocompletar → placeId
        String placeId = places.obtenerPlaceId(texto);

        // Paso 2 → Obtener detalles
        var detalles = places.obtenerDetalles(placeId);

        return detalles.formattedAddress;
    }

    @Override
    public void stop() throws Exception {
        // Cerrar servicios cuando la app termina
        places.cerrar();
        calculator.closeContext();
    }
}