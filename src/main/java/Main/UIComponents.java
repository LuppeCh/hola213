package Main;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Clase utilitaria que contiene métodos para crear componentes UI reutilizables.
 * Mantiene la consistencia visual en toda la aplicación.
 */
public class UIComponents {

    private final Stage stage;
    private static final String API_KEY = "AIzaSyB6uynr_3ELge4l5JrkDNGh3JYs-zO53DI";

    public UIComponents(Stage stage) {
        this.stage = stage;
    }

    // ========================================
    // BOTONES
    // ========================================

    public Button crearBotonPrincipal(String texto) {
        Button btn = new Button(texto);
        btn.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(40), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(50), " ", stage.widthProperty().divide(20), ";"
        ));
        return btn;
    }

    public Button crearBotonSecundario(String texto) {
        Button btn = new Button(texto);
        btn.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(45), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(60), " ", stage.widthProperty().divide(15), ";"
        ));
        return btn;
    }

    public Button crearBotonAccion(String texto) {
        Button btn = new Button(texto);
        btn.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(25), ";"
        ));
        return btn;
    }

    public Button crearBotonVolver() {
        Button btn = new Button("← Volver");
        btn.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
        return btn;
    }

    // ========================================
    // LABELS
    // ========================================

    public Label crearTituloSeccion(String texto) {
        Label lbl = new Label(texto);
        lbl.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(30), "px; -fx-font-weight: bold;"
        ));
        return lbl;
    }

    public Label crearLabelCampo(String texto) {
        Label lbl = new Label(texto);
        lbl.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));
        return lbl;
    }

    public Label crearLabelMensaje() {
        Label lbl = new Label("");
        lbl.setWrapText(true);
        lbl.setMaxWidth(600);
        lbl.setAlignment(Pos.CENTER);
        lbl.styleProperty().bind(Bindings.concat(
                "-fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));
        return lbl;
    }

    // ========================================
    // CAMPOS DE TEXTO
    // ========================================

    public TextField crearCampoTexto(String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        txt.maxWidthProperty().bind(stage.widthProperty().divide(4));
        txt.prefHeightProperty().bind(stage.heightProperty().divide(20));
        txt.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
        return txt;
    }

    // ========================================
    // MANEJO DE MENSAJES
    // ========================================

    public void mostrarMensajeError(Label label, String mensaje) {
        label.setText(mensaje);
        label.setTextFill(Color.RED);
        label.setVisible(true);
    }

    public void mostrarMensajeExito(Label label, String mensaje) {
        label.setText(mensaje);
        label.setTextFill(Color.GREEN);
        label.setVisible(true);
    }

    public void mostrarMensajeCarga(Label label, String mensaje) {
        label.setText(mensaje);
        label.setTextFill(Color.web("#2196F3"));
        label.setVisible(true);
    }

    // ========================================
    // ESTILOS
    // ========================================

    public void aplicarGradiente(VBox root, String colorInicio, String colorFin) {
        root.setStyle("-fx-background-color: linear-gradient(to bottom, " + colorInicio + " 0%, " + colorFin + " 100%);");
    }

    // ========================================
    // COMPONENTE DE MAPA
    // ========================================

    public VBox crearComponenteMapa(Ruta ruta) {
        Label lblMapa = new Label("📍 Visualización del recorrido:");
        lblMapa.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20 0 10 0;");

        javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
        webView.setPrefHeight(400);
        webView.setPrefWidth(650);

        String htmlContent = generarHTMLMapaIframe(ruta.linkMapa());
        webView.getEngine().loadContent(htmlContent);

        Button btnVerMapaCompleto = new Button("🗺️ Ver mapa completo en navegador");
        btnVerMapaCompleto.setStyle(
                "-fx-font-size: 13px; -fx-padding: 10 20; -fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;"
        );
        btnVerMapaCompleto.setOnAction(e -> abrirEnNavegador(ruta.linkMapa()));

        return new VBox(10, lblMapa, webView, btnVerMapaCompleto);
    }

    private String generarHTMLMapaIframe(String linkMapa) {
        String origen = "";
        String destino = "";

        if (linkMapa.contains("/dir/")) {
            try {
                String path = linkMapa.substring(linkMapa.indexOf("/dir/") + 5);
                String[] parts = path.split("/");
                if (parts.length >= 2) {
                    origen = parts[0];
                    destino = parts[1];
                }
            } catch (Exception e) {
                // Ignorar errores de parsing
            }
        }

        String embedUrl;
        if (!origen.isEmpty() && !destino.isEmpty()) {
            try {
                String encodedOrigin = java.net.URLEncoder.encode(origen, java.nio.charset.StandardCharsets.UTF_8);
                String encodedDestination = java.net.URLEncoder.encode(destino, java.nio.charset.StandardCharsets.UTF_8);

                embedUrl = String.format(
                        "https://www.google.com/maps/embed/v1/directions?key=%s&origin=%s&destination=%s",
                        API_KEY, encodedOrigin, encodedDestination
                );
            } catch (Exception e) {
                embedUrl = linkMapa;
            }
        } else {
            embedUrl = String.format(
                    "https://www.google.com/maps/embed/v1/view?key=%s&center=-32.94682,-60.63932&zoom=12",
                    API_KEY
            );
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { margin: 0; padding: 0; overflow: hidden; background-color: #f0f0f0; }
                    iframe { width: 100%%; height: 100%%; border: none; border-radius: 8px; }
                </style>
            </head>
            <body>
                <iframe src="%s" allowfullscreen loading="lazy"></iframe>
            </body>
            </html>
            """.formatted(embedUrl);
    }

    private void abrirEnNavegador(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            System.err.println("Error al abrir navegador: " + e.getMessage());
        }
    }
}
